package com.example.hotbedagrocontrolapp.presentation.viewModel.statistics

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotbedagrocontrolapp.data.db.DataBaseManager
import com.example.hotbedagrocontrolapp.domain.entities.statistics.AnaliseType
import com.example.hotbedagrocontrolapp.domain.entities.elements.Element
import com.example.hotbedagrocontrolapp.domain.entities.elements.Response
import com.example.hotbedagrocontrolapp.domain.entities.elements.SensorResponse
import com.example.hotbedagrocontrolapp.domain.entities.statistics.DateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.Locale
import kotlin.comparisons.compareBy

class StatisticsViewModel(
    private val dataBaseManager: DataBaseManager
) : ViewModel() {
    private val _dataHistory =
        mutableMapOf<HistoryItem, StateFlow<Map<String, Response>>>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDataHistory(
        element: Element,
        dateTime: DateTime
    ): StateFlow<Map<String, Response>> {
        _dataHistory[HistoryItem(element, dateTime)]?.let {
            return it
        }
        val flow =
            filterByAnaliseType(dataBaseManager.dataHistory[element] ?: emptyFlow(), dateTime)

        val stateFlow = flow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyMap()
        )
        _dataHistory[HistoryItem(element, dateTime)] = stateFlow
        return stateFlow
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterByAnaliseType(
        flow: Flow<List<Pair<LocalDateTime, Response>>>,
        dateTime: DateTime
    ): Flow<Map<String, Response>> {
        val comparator = if (dateTime.analiseType == AnaliseType.YEAR) compareBy<String> {
            val formatter = DateTimeFormatter.ofPattern("LLLL", Locale("ru"))
            val month = formatter.parse(it)
            month.get(ChronoField.MONTH_OF_YEAR)
        } else compareBy { it }

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
                        .filter { (it.first.dayOfMonth - 1) % 5 == 0 }
                }

                AnaliseType.DAY -> {
                    list.filter {
                        it.first.year == dateTime.localDateTime.year
                                && it.first.month == dateTime.localDateTime.month
                                && (it.first.dayOfMonth == dateTime.localDateTime.dayOfMonth
                                || (it.first.dayOfMonth == dateTime.localDateTime.plusDays(1).dayOfMonth && it.first.hour == 0))
                    }
                        .distinctBy { it.first.hour }
                        .filter { it.first.hour % 3 == 0 }
                }

                AnaliseType.HOUR -> {
                    list.filter {
                        it.first.year == dateTime.localDateTime.year
                                && it.first.month == dateTime.localDateTime.month
                                && it.first.dayOfMonth == dateTime.localDateTime.dayOfMonth
                                && (it.first.hour == dateTime.localDateTime.hour
                                || (it.first.hour == dateTime.localDateTime.plusHours(1).hour) && it.first.minute == 0)
                    }
                        .distinctBy { it.first.minute }
                        .filter { it.first.minute % 10 == 0 }
                }
            }.associate {
                when (dateTime.analiseType) {
                    AnaliseType.HOUR -> it.first.format(DateTimeFormatter.ofPattern("HH:mm"))
                    AnaliseType.DAY -> it.first.format(DateTimeFormatter.ofPattern("HHч"))
                    AnaliseType.MONTH -> it.first.format(DateTimeFormatter.ofPattern("dd.MM"))
                    AnaliseType.YEAR -> it.first.format(
                        DateTimeFormatter.ofPattern(
                            "LLLL",
                            Locale("ru")
                        )
                    )
                } to it.second
            }
                .toSortedMap(comparator)

            val range = when (dateTime.analiseType) {
                AnaliseType.YEAR -> 1L..12L
                AnaliseType.MONTH -> 0L .. 31L step 5
                AnaliseType.DAY -> 0L until 24L step 3
                AnaliseType.HOUR -> 0L..60L step 10
            }

            val iterator = dateTime.iterator
            for (i in range) {
                map.putIfAbsent(iterator.plus(i), SensorResponse(0.0))
            }

            map
        }
    }
}

data class HistoryItem(
    val element: Element,
    val dateTime: DateTime
)