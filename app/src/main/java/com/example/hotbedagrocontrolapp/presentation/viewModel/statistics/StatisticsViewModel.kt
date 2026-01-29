package com.example.hotbedagrocontrolapp.presentation.viewModel.statistics

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotbedagrocontrolapp.data.db.DataBaseManager
import com.example.hotbedagrocontrolapp.domain.entities.elements.ControlResponse
import com.example.hotbedagrocontrolapp.domain.entities.statistics.AnaliseType
import com.example.hotbedagrocontrolapp.domain.interfaces.entities.elements.Element
import com.example.hotbedagrocontrolapp.domain.entities.elements.Response
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.elements.SensorResponse
import com.example.hotbedagrocontrolapp.domain.entities.statistics.DateTime
import com.example.hotbedagrocontrolapp.presentation.ui.components.statistics.STATISTICS_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.Locale
import javax.inject.Inject
import kotlin.comparisons.compareBy

/**
 * Бизнес-логика для работы с историей изменений и графиками.
 *
 * @param dataBaseManager Менеджер базы данных.
 */
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val dataBaseManager: DataBaseManager
) : ViewModel() {
    private val _dataHistory =
        mutableMapOf<HistoryItem, StateFlow<Map<LocalDateTime, Response>>>()

    /**
     * Получить историю изменения данных.
     *
     * @param element Элемент.
     * @param dateTime Дата-время.
     *
     * @return Мапа времени и значения элемента.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDataHistory(
        element: Element,
        dateTime: DateTime
    ): StateFlow<Map<LocalDateTime, Response>> {
        try {
            _dataHistory[HistoryItem(element, dateTime)]?.let {
                return it
            }
            val flow =
                filterByAnaliseType(element, dataBaseManager.dataHistory[element] ?: emptyFlow(), dateTime)

            val stateFlow = flow.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyMap()
            )
            _dataHistory[HistoryItem(element, dateTime)] = stateFlow
            return stateFlow
        } catch (e: Exception) {
            Log.e(STATISTICS_TAG, "Error getting data history: ${e.message}.")
            return MutableStateFlow<Map<LocalDateTime, Response>>(emptyMap()).asStateFlow()
        }
    }

    /**
     * Отфильтровать историю изменений по типу анализа и дате.
     *
     * @param flow Поток с историей изменений.
     * @param dateTime Дата-время.
     *
     * @return Отфильтрованный поток истории изменений.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterByAnaliseType(
        element: Element,
        flow: Flow<List<Pair<LocalDateTime, Response>>>,
        dateTime: DateTime
    ): Flow<Map<LocalDateTime, Response>> {
        return flow.map { list ->
            val map = when (dateTime.analiseType) {
                AnaliseType.YEAR -> {
                    list.filter { it.first.year == dateTime.localDateTime.year }
                        .distinctBy { it.first.month }
                }

                AnaliseType.MONTH -> {
                    list.filter {
                        it.first.year == dateTime.localDateTime.year
                                && it.first.month == dateTime.localDateTime.month
                    }
                        .distinctBy { it.first.dayOfMonth }
                }

                AnaliseType.DAY -> {
                    list.filter {
                        it.first.year == dateTime.localDateTime.year
                                && it.first.month == dateTime.localDateTime.month
                                && it.first.dayOfMonth == dateTime.localDateTime.dayOfMonth
                    }
                        .distinctBy { it.first.hour }
                }

                AnaliseType.HOUR -> {
                    list.filter {
                        it.first.year == dateTime.localDateTime.year
                                && it.first.month == dateTime.localDateTime.month
                                && it.first.dayOfMonth == dateTime.localDateTime.dayOfMonth
                                && it.first.hour == dateTime.localDateTime.hour
                    }
                        .distinctBy { it.first.minute }
                }
            }.associate { when (dateTime.analiseType) {
                AnaliseType.YEAR -> LocalDateTime.of(it.first.year, it.first.month, 1, 0, 0)
                AnaliseType.MONTH -> LocalDateTime.of(it.first.year, it.first.month, it.first.dayOfMonth, 0, 0)
                AnaliseType.DAY -> LocalDateTime.of(it.first.year, it.first.month, it.first.dayOfMonth, it.first.hour, 0)
                AnaliseType.HOUR -> LocalDateTime.of(it.first.year, it.first.month, it.first.dayOfMonth, it.first.hour, it.first.minute)
            } to it.second }
                .toSortedMap()

            val range = when (dateTime.analiseType) {
                AnaliseType.YEAR -> 1L until 12L
                AnaliseType.MONTH -> 0L until 30L
                AnaliseType.DAY -> 0L until 24L
                AnaliseType.HOUR -> 0L until 60L
            }

            val iterator = dateTime.iterator
            for (i in range) {
                map.putIfAbsent(iterator.plus(i),
                    if (element is Sensor) SensorResponse(0.0)
                    else ControlResponse(ControlResponse.Status.NaN)
                )
            }

            map
        }
    }
}

data class HistoryItem(
    val element: Element,
    val dateTime: DateTime
)