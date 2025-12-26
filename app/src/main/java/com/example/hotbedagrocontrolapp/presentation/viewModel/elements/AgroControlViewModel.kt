package com.example.hotbedagrocontrolapp.presentation.viewModel.elements

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotbedagrocontrolapp.data.db.DataBaseManager
import com.example.hotbedagrocontrolapp.domain.entities.elements.Control
import com.example.hotbedagrocontrolapp.domain.entities.elements.ControlResponse
import com.example.hotbedagrocontrolapp.domain.entities.elements.Element
import com.example.hotbedagrocontrolapp.domain.entities.elements.Response
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.elements.SensorResponse
import com.example.hotbedagrocontrolapp.domain.interfaces.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class AgroControlViewModel(
    private val dataBaseManager: DataBaseManager,
    private val mqttClient: Client
) : ViewModel() {
    private val _currentData = MutableStateFlow<MutableMap<Element, Response>>(mutableMapOf())
    val currentData = _currentData.asStateFlow()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mqttClient.connect(::onMessageReceived)
                Log.d(Client.Companion.CLIENT_TAG, "Connected!")
            } catch (e: Exception) {
                Log.e(Client.Companion.CLIENT_TAG, "Connection error: ${e.message}")
            }
        }
    }

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

    fun publish(control: Control, status: ControlResponse.Status) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mqttClient.publish(control.topic, status.message)
            } catch (e: Exception) {
                Log.e(Client.Companion.CLIENT_TAG, "Error while publishing data: ${e.message}.")
            }
        }
    }

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

    fun onStatusChanged(control: Control, isControlOn: Boolean) {
        when (isControlOn) {
            true -> publish(control, ControlResponse.Status.ON)
            false -> publish(control, ControlResponse.Status.OFF)
        }
    }

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

    private fun defineResponse(responseString: String): Response? {
        ControlResponse.Status.entries.map { status ->
            if (status.message == responseString) {
                return ControlResponse(status)
            }
        }
        val responseDouble = responseString.toDoubleOrNull()
        return responseDouble?.let { SensorResponse(responseDouble) }
    }

    fun disconnect() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mqttClient.disconnect()
            } catch (e: Exception) {
                Log.e(Client.Companion.CLIENT_TAG, "Disconnection error: ${e.message}")
            }
        }
    }
}