package com.example.hotbedagrocontrolapp.presentation.viewModel.elements

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotbedagrocontrolapp.data.db.DataBaseManager
import com.example.hotbedagrocontrolapp.domain.entities.devices.MqttSettings
import com.example.hotbedagrocontrolapp.domain.entities.elements.Control
import com.example.hotbedagrocontrolapp.domain.entities.elements.ControlResponse
import com.example.hotbedagrocontrolapp.domain.interfaces.entities.elements.Element
import com.example.hotbedagrocontrolapp.domain.entities.elements.Response
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.elements.SensorResponse
import com.example.hotbedagrocontrolapp.domain.interfaces.data.service.Client
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
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
    private val mqttClient: Client,
    @ApplicationContext private val ctx: Context
) : ViewModel() {
    private val _currentData = MutableStateFlow<MutableMap<Element, Response>>(mutableMapOf())
    val currentData = _currentData.asStateFlow()

    private val _isConnected = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    private val prefs = ctx.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _isDeviceAdded = MutableStateFlow(prefs.getString("ip_address", null) != null)
    val isDeviceAdded = _isDeviceAdded.asStateFlow()

    val mqttSettings: MqttSettings
        get() = MqttSettings(
            ipAddress = prefs.getString("ip_address", "") ?: "",
            mainTopic = prefs.getString("main_topic", "") ?: "",
            userName = prefs.getString("user_name", "") ?: "",
            password = prefs.getString("password", "") ?: ""
        )

    init {
        if (_isDeviceAdded.value) {
            connect()
        }
    }

    /**
     * Подключиться к устройству по Mqtt.
     */
    private fun connect() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mqttClient.connect(::onMessageReceived) { _isConnected.value = false }
                _isConnected.value = true
                Log.d(Client.Companion.CLIENT_TAG, "Connected!")
            } catch (e: Exception) {
                Log.e(Client.Companion.CLIENT_TAG, "Connection error: ${e.message}")
            }
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
    fun addDevice(ipAddress: String, mainTopic: String, userName: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            prefs.edit {
                putString("ip_address", ipAddress)
                putString("main_topic", mainTopic)
                putString("user_name", userName)
                putString("password", password)
            }
            _isDeviceAdded.value = true
            Log.d(Client.Companion.CLIENT_TAG, "Device was added!")
            connect()
        }
    }

    /**
     * Удалить устройство.
     */
    fun deleteDevice() {
        viewModelScope.launch(Dispatchers.IO) {
            prefs.edit {
                clear()
            }
            _isDeviceAdded.value = false
            Log.d(Client.Companion.CLIENT_TAG, "Device was deleted!")
            disconnect()
        }
    }

    /**
     * Вставить в таблицу текущее значение элемента.
     *
     * @param element Элемент.
     * @param response Значение элемента, полученное с устройства.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertCurrentData(element: Element, response: Response) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentTime = LocalDateTime.of(
                    LocalDate.now(),
                    LocalTime.of(
                        LocalDateTime.now().hour,
                        LocalDateTime.now().minute
                    )
                )
                dataBaseManager.insertData(element, response, currentTime)
            } catch (e: Exception) {
                Log.e(DataBaseManager.Companion.DATA_BASE_TAG, "Error while inserting new data in db: ${e.message}.")
            }
        }
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
                mqttClient.publish(control.topic, status.message)
            } catch (e: Exception) {
                Log.e(Client.Companion.CLIENT_TAG, "Error while publishing data: ${e.message}.")
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
            Log.e(Client.Companion.CLIENT_TAG, "Unknown topic: $topicString.")
            return
        }
        val response = defineResponse(responseString)
        if (response == null) {
            Log.e(Client.Companion.CLIENT_TAG, "Unknown response: $responseString.")
            return
        }
        val newMap = _currentData.value.toMutableMap()
        newMap[element] = response
        _currentData.value = newMap
        insertCurrentData(element, response)
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
        disconnect()
    }

    /**
     * Отсоединиться от Mosquitto.
     */
    fun disconnect() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mqttClient.disconnect()
                _isConnected.value = false
                Log.d(Client.Companion.CLIENT_TAG, "Disconnected!")
            } catch (e: Exception) {
                Log.e(Client.Companion.CLIENT_TAG, "Disconnection error: ${e.message}")
            }
        }
    }
}