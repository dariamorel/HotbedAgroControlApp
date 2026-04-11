package com.example.hotbedagrocontrolapp.domain.viewModel.elements

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotbedagrocontrolapp.di.db.DataBaseManager
import com.example.hotbedagrocontrolapp.data.service.dataService.DataServiceManager
import com.example.hotbedagrocontrolapp.data.service.MqttClient
import com.example.hotbedagrocontrolapp.data.service.dataService.DataServiceClient
import com.example.hotbedagrocontrolapp.domain.entities.devices.MqttSettings
import com.example.hotbedagrocontrolapp.domain.entities.elements.Control
import com.example.hotbedagrocontrolapp.domain.entities.elements.ControlResponse
import com.example.hotbedagrocontrolapp.domain.interfaces.entities.elements.Element
import com.example.hotbedagrocontrolapp.domain.entities.elements.Response
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.elements.SensorResponse
import com.example.hotbedagrocontrolapp.domain.entities.statistics.AnaliseType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Бизнес-логика для работы с элементами (датчиками и элементами управления).
 *
 * @param dataBaseManager Менеджер базы данных.
 * @param mqttClient Mqtt клиент.
 */
@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class AgroControlViewModel @Inject constructor(
    private val dataBaseManager: DataBaseManager,
    private val dataRepository: DataServiceManager,
    private val dataServiceManager: DataServiceManager,
    @ApplicationContext private val ctx: Context
) : ViewModel() {
    private val _currentData = MutableStateFlow<MutableMap<Element, Response>>(mutableMapOf())
    val currentData = _currentData.asStateFlow()

    private val _isConnected = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    private val _connectionError = MutableStateFlow(false)
    val connectionError = _connectionError.asStateFlow()

    private val prefs = ctx.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _isDeviceAdded = MutableStateFlow(hasSavedMqttSettings())
    val isDeviceAdded = _isDeviceAdded.asStateFlow()
    private val _optimalValues = MutableStateFlow(loadOptimalValues())
    val optimalValues = _optimalValues.asStateFlow()
    private var currentMqttClient: MqttClient? = null

    val mqttSettings: MqttSettings
        get() = MqttSettings(
            ipAddress = prefs.getString("ip_address", "") ?: "",
            mainTopic = prefs.getString("main_topic", "") ?: "",
            userName = prefs.getString("user_name", "") ?: "",
            password = prefs.getString("password", "") ?: "",
            port = prefs.getString("port", "") ?: ""
        )

    init {
        if (_isDeviceAdded.value) {
            viewModelScope.launch(Dispatchers.IO) {
                connect(mqttSettings)
            }
        }
    }

    /**
     * Подключиться к устройству по Mqtt.
     */
    private suspend fun connect(settings: MqttSettings): Boolean {
        try {
            currentMqttClient?.disconnect()
        } catch (_: Exception) {
        }

        val mqttClient = MqttClient(
            settings.ipAddress,
            settings.mainTopic,
            settings.userName,
            settings.password,
            settings.port
        )

        return try {
            mqttClient.connect(::onMessageReceived) {
                _isConnected.value = false
            }
            currentMqttClient = mqttClient
            _isConnected.value = true
            _connectionError.value = false
            Log.d(MqttClient.Companion.CLIENT_TAG, "Connected!")
            true
        } catch (e: Exception) {
            currentMqttClient = null
            _isConnected.value = false
            _connectionError.value = true
            Log.e(MqttClient.Companion.CLIENT_TAG, "Connection error: ${e.message}")
            false
        }
    }

    /**
     * Добавить устройство.
     *
     * @param ipAddress IP адресс Mosquitto.
     * @param mainTopic Главный топик Mosquitto.
     * @param userName Имя пользователя в Mosquitto.
     * @param password Пароль пользователя в Mosquitto.
     */
    fun addDevice(ipAddress: String, mainTopic: String, userName: String, password: String, port: String) {
        viewModelScope.launch(Dispatchers.IO) {
            removeConnectionError()

            val settings = MqttSettings(
                ipAddress = ipAddress,
                mainTopic = mainTopic,
                userName = userName,
                password = password,
                port = port
            )

            val isConnected = connect(settings)
            if (!isConnected) {
                _isDeviceAdded.value = false
                return@launch
            }

            saveMqttSettings(settings)
            _isDeviceAdded.value = true
            Log.d(MqttClient.Companion.CLIENT_TAG, "Device was added!")

            try {
                dataRepository.createUser(ipAddress, mainTopic, userName, password, port.toInt())
            } catch (e: Exception) {
                Log.e(DataServiceClient.DATA_SERVICE_TAG, "Error while creating user: ${e.message}.")
            }
        }
    }

    /**
     * Удалить устройство.
     */
    fun deleteDevice() {
        viewModelScope.launch(Dispatchers.IO) {
            disconnect()
            try {
                dataRepository.deleteUser()
            } catch (e: Exception) {
                Log.e(DataServiceClient.DATA_SERVICE_TAG, "Error while deleting user: ${e.message}.")
            }
            prefs.edit {
                clear()
            }
            _currentData.value = mutableMapOf()
            _optimalValues.value = loadOptimalValues()
            _isDeviceAdded.value = false
            _connectionError.value = false
            Log.d(MqttClient.Companion.CLIENT_TAG, "Device was deleted!")
        }
    }

    fun saveOptimalValues(values: Map<Sensor, String>) {
        prefs.edit {
            values.forEach { (sensor, value) ->
                val normalizedValue = value.replace(',', '.').trim()
                if (normalizedValue.toDoubleOrNull() != null) {
                    putString(optimalValueKey(sensor), normalizedValue)
                } else {
                    remove(optimalValueKey(sensor))
                }
            }
        }
        _optimalValues.value = loadOptimalValues()
    }

    /**
     * Опубликовать новый статус элемента управления в Mosquitto.
     *
     * @param control Элемент управления.
     * @param status Новый статус.
     */
    fun publish(control: Control, status: ControlResponse.Status) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                currentMqttClient?.publish(control.topic, status.message)
                dataServiceManager.getDataHistory(
                    control,
                    LocalDateTime.now(),
                    AnaliseType.DAY
                )
            } catch (e: Exception) {
                Log.e(MqttClient.Companion.CLIENT_TAG, "Error while publishing data: ${e.message}.")
            }
        }
    }

    /**
     * Обработка получаемого значения с устройства.
     *
     * @param topicString Название топика в Mosquitto.
     * @param responseString Полученное новое значение.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun onMessageReceived(topicString: String, responseString: String) {
        val element = defineElement(topicString)
        if (element == null) {
            Log.e(MqttClient.Companion.CLIENT_TAG, "Unknown topic: $topicString.")
            return
        }
        val response = defineResponse(responseString)
        if (response == null) {
            Log.e(MqttClient.Companion.CLIENT_TAG, "Unknown response: $responseString.")
            return
        }
        val newMap = _currentData.value.toMutableMap()
        newMap[element] = response
        _currentData.value = newMap
    }

    /**
     * Обработка изменения статуса элемента управления.
     *
     * @param control Элемент управления.
     * @param isControlOn Включен ли элемент управления.
     */
    fun onStatusChanged(control: Control, isControlOn: Boolean) {
        when (isControlOn) {
            true -> publish(control, ControlResponse.Status.ON)
            false -> publish(control, ControlResponse.Status.OFF)
        }
    }

    /**
     * Определить элемент по топику.
     *
     * @param topicString Топик.
     */
    private fun defineElement(topicString: String): Element? {
        Sensor.entries.map { sensor ->
            if (topicString.contains("/${sensor.topic}/")) {
                return sensor
            }
        }
        Control.entries.map { control ->
            if (topicString.contains("/${control.topic}/")) {
                return control
            }
        }
        return null
    }

    /**
     * Определить полученное значение.
     *
     * @param responseString Полученное значение.
     */
    private fun defineResponse(responseString: String): Response? {
        ControlResponse.Status.entries.map { status ->
            if (status.message == responseString) {
                return ControlResponse(status)
            }
        }
        val responseDouble = responseString.toDoubleOrNull()
        return responseDouble?.let { SensorResponse(responseDouble) }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch(Dispatchers.IO) {
            disconnect()
        }
    }

    /**
     * Отсоединиться от Mosquitto.
     */
    suspend fun disconnect() {
        try {
            currentMqttClient?.disconnect()
            currentMqttClient = null
            _isConnected.value = false
            Log.d(MqttClient.Companion.CLIENT_TAG, "Disconnected!")
        } catch (e: Exception) {
            Log.e(MqttClient.Companion.CLIENT_TAG, "Disconnection error: ${e.message}")
        }
    }

    fun removeConnectionError() {
        _connectionError.value = false
    }

    private fun hasSavedMqttSettings(): Boolean {
        return mqttSettings.ipAddress.isNotBlank() &&
                mqttSettings.mainTopic.isNotBlank() &&
                mqttSettings.userName.isNotBlank() &&
                mqttSettings.password.isNotBlank() &&
                mqttSettings.port.isNotBlank()
    }

    private fun saveMqttSettings(settings: MqttSettings) {
        prefs.edit {
            putString("ip_address", settings.ipAddress)
            putString("main_topic", settings.mainTopic)
            putString("user_name", settings.userName)
            putString("password", settings.password)
            putString("port", settings.port)
        }
    }

    private fun loadOptimalValues(): Map<Sensor, Double?> =
        Sensor.entries.associateWith { sensor ->
            prefs.getString(optimalValueKey(sensor), null)?.toDoubleOrNull()
        }

    private fun optimalValueKey(sensor: Sensor): String = "optimal_value_${sensor.name.lowercase()}"
}