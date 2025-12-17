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
import com.example.hotbedagrocontrolapp.domain.entities.HistoryBy
import com.example.hotbedagrocontrolapp.domain.entities.Response
import com.example.hotbedagrocontrolapp.domain.entities.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.SensorResponse
import com.example.hotbedagrocontrolapp.domain.interfaces.Client
import com.example.hotbedagrocontrolapp.domain.interfaces.Client.Companion.CLIENT_TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class AgroControlViewModel(
    private val dataBaseManager: DataBaseManager,
    private val mqttClient: Client
) : ViewModel() {
    private val _currentData = MutableStateFlow<MutableMap<Element, Response>>(mutableMapOf())
    val currentData = _currentData.asStateFlow()
    private val _dataHistory =
        mutableMapOf<HistoryItem, StateFlow<Map<String, Response>>>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mqttClient.connect(::onMessageReceived)
                Log.d(CLIENT_TAG, "Connected!")
            } catch (e: Exception) {
                Log.e(CLIENT_TAG, "Connection error: ${e.message}")
            }
        }
    }

    fun getDataHistory(
        element: Element,
        by: HistoryBy,
        dateTime: LocalDateTime
    ): StateFlow<Map<String, Response>> {
        _dataHistory[HistoryItem(element, by, dateTime)]?.let {
            return it
        }
        val flow = filterBy(dataBaseManager.dataHistory[element] ?: emptyFlow(), by, dateTime)

        val stateFlow = flow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyMap()
        )
        _dataHistory[HistoryItem(element, by, dateTime)] = stateFlow
        return stateFlow
    }

    private fun filterBy(
        flow: Flow<List<Pair<LocalDateTime, Response>>>,
        by: HistoryBy,
        dateTime: LocalDateTime
    ): Flow<Map<String, Response>> {
        return flow.map { list ->
            when (by) {
                HistoryBy.YEAR -> {
                    val map = list.filter { it.first.year == dateTime.year }
                        .distinctBy { it.first.month }
                        .associate { it.first.format(DateTimeFormatter.ofPattern("MM")) to it.second }
                        .toSortedMap()
                    for (i in 1L..12L) {
                        val dateItem = dateTime.plusMonths(i)
                            .format(DateTimeFormatter.ofPattern("MM"))

                        map.putIfAbsent(dateItem, SensorResponse(0.0))
                    }
                    map
                }

                HistoryBy.MONTH -> {
                    val map = list.filter {
                        it.first.year == dateTime.year
                                && it.first.month == dateTime.month
                    }
                        .distinctBy { it.first.dayOfMonth }
                        .filter { it.first.dayOfMonth % 2 == 0 }
                        .associate { it.first.format(DateTimeFormatter.ofPattern("dd")) to it.second }
                        .toSortedMap()
                    for (i in 1L until 31L step 2) {
                        val dateItem = dateTime.plusDays(i)
                            .format(DateTimeFormatter.ofPattern("dd"))

                        map.putIfAbsent(dateItem, SensorResponse(0.0))
                    }
                    map
                }

                HistoryBy.DAY -> {
                    val map = list.filter {
                        it.first.year == dateTime.year
                                && it.first.month == dateTime.month
                                && it.first.dayOfMonth == dateTime.dayOfMonth
                    }
                        .distinctBy { it.first.hour }
                        .filter { it.first.hour % 2 == 0 }
                        .associate { it.first.format(DateTimeFormatter.ofPattern("HH")) to it.second }
                        .toSortedMap()
                    for (i in 0L until 24L step 2) {
                        val dateItem = dateTime.plusHours(i)
                            .format(DateTimeFormatter.ofPattern("HH"))

                        map.putIfAbsent(dateItem, SensorResponse(0.0))
                    }
                    map
                }

                HistoryBy.HOUR -> {
                    val map = list.filter {
                        it.first.year == dateTime.year
                                && it.first.month == dateTime.month
                                && it.first.dayOfMonth == dateTime.dayOfMonth
                                && it.first.hour == dateTime.hour
                    }
                        .distinctBy { it.first.minute }
                        .filter { it.first.minute % 5 == 0 }
                        .associate { it.first.format(DateTimeFormatter.ofPattern("mm")) to it.second }
                        .toSortedMap()
                    for (i in 0L until 60L step 5) {
                        val dateItem = dateTime.plusMinutes(i)
                            .format(DateTimeFormatter.ofPattern("mm"))

                        map.putIfAbsent(dateItem, SensorResponse(0.0))
                    }
                    map
                }
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
            } catch (e: Exception) {
                Log.e(CLIENT_TAG, "Disconnection error: ${e.message}")
            }
        }
    }
}

data class HistoryItem(
    val element: Element,
    val by: HistoryBy,
    val dateTime: LocalDateTime
)