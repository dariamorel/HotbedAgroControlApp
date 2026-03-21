package com.example.hotbedagrocontrolapp.presentation.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.statistics.AnaliseType
import com.example.hotbedagrocontrolapp.domain.entities.statistics.DateTime
import com.example.hotbedagrocontrolapp.presentation.ui.components.statistics.LineGraph
import com.example.hotbedagrocontrolapp.presentation.ui.components.statistics.SwitchAnaliseType
import com.example.hotbedagrocontrolapp.presentation.ui.components.statistics.SwitchDateTime
import com.example.hotbedagrocontrolapp.presentation.ui.components.statistics.SwitchElement
import com.example.hotbedagrocontrolapp.presentation.viewModel.statistics.StatisticsViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs

/**
 * Экран с графиками.
 *
 * @param viewModel Бизнес-логика для работы с историей изменений и графиками.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsGraphScreen(
    viewModel: StatisticsViewModel,
    modifier: Modifier = Modifier
) {
    var sensor by remember { mutableStateOf(Sensor.AIR_TEMPERATURE) }
    var dateTime by remember { mutableStateOf(DateTime(AnaliseType.DAY)) }
    val values by viewModel.getDataHistory(sensor, dateTime).collectAsState()
    var accumulatedPanX by remember { mutableStateOf(0f) }
    var accumulatedZoom by remember { mutableStateOf(1f) }

    fun setAnaliseType(newAnaliseType: AnaliseType) {
        val now = LocalDateTime.now()
        val newDateTime = if (when (newAnaliseType) {
                AnaliseType.YEAR -> false
                AnaliseType.MONTH -> dateTime.localDateTime.year == now.year
                AnaliseType.DAY -> dateTime.localDateTime.year == now.year && dateTime.localDateTime.month == now.month
                AnaliseType.HOUR -> dateTime.localDateTime.year == now.year && dateTime.localDateTime.month == now.month && dateTime.localDateTime.dayOfMonth == now.dayOfMonth
            }) now else dateTime.localDateTime
        dateTime = DateTime(newAnaliseType, newDateTime)
    }

    fun zoomInAnaliseType() {
        val next = when (dateTime.analiseType) {
            AnaliseType.YEAR -> AnaliseType.MONTH
            AnaliseType.MONTH -> AnaliseType.DAY
            AnaliseType.DAY -> AnaliseType.HOUR
            AnaliseType.HOUR -> AnaliseType.HOUR
        }
        if (next != dateTime.analiseType) setAnaliseType(next)
    }

    fun zoomOutAnaliseType() {
        val next = when (dateTime.analiseType) {
            AnaliseType.YEAR -> AnaliseType.YEAR
            AnaliseType.MONTH -> AnaliseType.YEAR
            AnaliseType.DAY -> AnaliseType.MONTH
            AnaliseType.HOUR -> AnaliseType.DAY
        }
        if (next != dateTime.analiseType) setAnaliseType(next)
    }

    Column(
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            SwitchElement(
                element = sensor,
                options = Sensor.entries,
                modifier = Modifier.weight(2f)
            ) { selected ->
                sensor = selected as Sensor
            }
            SwitchAnaliseType(dateTime.analiseType, Modifier.weight(1f)) { newAnaliseType ->
                setAnaliseType(newAnaliseType)
            }
        }

        SwitchDateTime(dateTime, Modifier.align(Alignment.End)) { newDateTime ->
            dateTime = newDateTime
        }

        val labels = when (dateTime.analiseType) {
            AnaliseType.HOUR -> values.map { (key, _) ->
                if (key.minute % 10 == 0) key.format(DateTimeFormatter.ofPattern("HH:mm")) else " "
            }
            AnaliseType.DAY -> values.map { (key, _) ->
                if (key.hour % 3 == 0) key.format(DateTimeFormatter.ofPattern("HHч")) else " "
            }
            AnaliseType.MONTH -> values.map { (key, _) ->
                if ((key.dayOfMonth - 1) % 5 == 0) key.format(DateTimeFormatter.ofPattern("dd.MM")) else " "
            }
            AnaliseType.YEAR -> values.map { (key, _) ->
                key.format(DateTimeFormatter.ofPattern("LLLL", Locale("ru")))
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(dateTime.analiseType) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        accumulatedPanX += pan.x
                        accumulatedZoom *= zoom

                        // Бесконечная навигация: при горизонтальном сдвиге листаем период.
                        while (abs(accumulatedPanX) >= 90f) {
                            dateTime = if (accumulatedPanX > 0f) {
                                dateTime.minus(1)
                            } else {
                                dateTime.plus(1)
                            }
                            accumulatedPanX += if (accumulatedPanX > 0f) -90f else 90f
                        }

                        // Zoom-in -> более детальный период, zoom-out -> более широкий период.
                        if (accumulatedZoom > 1.12f) {
                            zoomInAnaliseType()
                            accumulatedZoom = 1f
                        } else if (accumulatedZoom < 0.88f) {
                            zoomOutAnaliseType()
                            accumulatedZoom = 1f
                        }
                    }
                }
        ) {
            LineGraph(sensor, values.map { (_, response) -> response }, labels)
        }
    }
}
