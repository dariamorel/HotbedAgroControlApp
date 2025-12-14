package com.example.hotbedagrocontrolapp.presentation.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotbedagrocontrolapp.data.db.DataBaseManager
import com.example.hotbedagrocontrolapp.data.db.DataBaseManager.Companion.DATA_BASE_TAG
import com.example.hotbedagrocontrolapp.domain.entities.Control
import com.example.hotbedagrocontrolapp.domain.entities.ControlResponse
import com.example.hotbedagrocontrolapp.domain.entities.Element
import com.example.hotbedagrocontrolapp.domain.entities.Response
import com.example.hotbedagrocontrolapp.domain.entities.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.SensorResponse
import com.example.hotbedagrocontrolapp.domain.interfaces.Client
import com.example.hotbedagrocontrolapp.domain.interfaces.Client.Companion.CLIENT_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
class AgroControlViewModel(
    private val dataBaseManager: DataBaseManager,
    private val mqttClient: Client
) : ViewModel() {
    private val _currentData = MutableStateFlow<MutableMap<Element, Response>>(mutableMapOf())
    val currentData = _currentData.asStateFlow()
    private val _dataHistory = mutableMapOf<Element, StateFlow<List<Pair<LocalDateTime, Response>>>>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mqttClient.connect(::onMessageReceived)
                Log.d(CLIENT_TAG, "Connected!")
            } catch(e: Exception) {
                Log.e(CLIENT_TAG, "Connection error: ${e.message}")
            }
        }
    }

    fun getDataHistory(element: Element): StateFlow<List<Pair<LocalDateTime, Response>>> {
        if (_dataHistory[element] != null) {
            return _dataHistory[element]!!
        }
        val flow = dataBaseManager.dataHistory[element] ?: emptyFlow()
        val stateFlow = flow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )
        _dataHistory[element] = stateFlow
        return stateFlow
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertCurrentData(element: Element, response: Response) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentTime = LocalDateTime.now()
                dataBaseManager.insertData(element, response, currentTime)
            } catch (e: Exception) {
                Log.e(DATA_BASE_TAG, "Error while inserting new data in db: ${e.message}.")
            }
        }
    }

    fun publish(control: Control, status: ControlResponse.Status) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mqttClient.publish(control.topic, status.message)
            } catch (e: Exception) {
                Log.e(CLIENT_TAG, "Error while publishing data: ${e.message}.")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onMessageReceived(topicString: String, responseString: String) {
        val element = defineElement(topicString)
        if (element == null) {
            Log.e(CLIENT_TAG, "Unknown topic: $topicString.")
            return
        }
        val response = defineResponse(responseString)
        if (response == null) {
            Log.e(CLIENT_TAG, "Unknown response: $responseString.")
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
            } catch(e: Exception) {
                Log.e(CLIENT_TAG, "Disconnection error: ${e.message}")
            }
        }
    }
}