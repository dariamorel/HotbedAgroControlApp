package com.example.hotbedagrocontrolapp.presentation.components.statistics

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.hotbedagrocontrolapp.domain.entities.elements.Response
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.statistics.DateTime
import com.example.hotbedagrocontrolapp.ui.theme.DarkBrown
import com.example.hotbedagrocontrolapp.ui.theme.DarkGreen
import com.example.hotbedagrocontrolapp.ui.theme.DarkOrange
import com.example.hotbedagrocontrolapp.ui.theme.DarkRed
import com.example.hotbedagrocontrolapp.ui.theme.MediumGrey
import com.example.hotbedagrocontrolapp.ui.theme.SkyBlue
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties
import kotlin.math.abs

const val STATISTICS_TAG = "Statistics"

/**
 * Отображение графика с историей изменений по конкретному датчику.
 *
 * @param sensor Датчик.
 * @param values Значения датчика.
 * @param labels Отметки на оси Ox.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LineGraph(
    sensor: Sensor,
    values: List<Response>,
    labels: List<String>,
    modifier: Modifier = Modifier,
    optimalValue: Double? = null
) {
    val chartValues = remember(values, sensor) {
        values.map { response ->
            response.dataToDouble.coerceIn(sensor.minValue, sensor.maxValue)
        }
    }
    val lineBrush = remember(chartValues, sensor, optimalValue) {
        chartLineBrush(
            sensor = sensor,
            values = chartValues,
            optimalValue = optimalValue
        )
    }
    val fillColor = remember(chartValues, sensor, optimalValue) {
        chartFillColor(
            sensor = sensor,
            values = chartValues,
            optimalValue = optimalValue
        )
    }

    LineChart(
        modifier = modifier
            .padding(bottom = 10.dp)
            .fillMaxSize(),
        data = remember(chartValues, sensor, lineBrush, fillColor) {
            listOf(
                Line(
                    label = sensor.elementName,
                    values = chartValues,
                    color = lineBrush,
                    firstGradientFillColor = fillColor,
                    secondGradientFillColor = Color.Transparent,
                    strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                    gradientAnimationDelay = 1000,
                    drawStyle = DrawStyle.Stroke(width = 2.dp),
                )
            )
        },
        animationMode = AnimationMode.Together(delayBuilder = {
            it * 500L
        }),
        minValue = sensor.minValue,
        maxValue = sensor.maxValue,
        labelProperties = LabelProperties(
            enabled = true,
            labels = labels,
            textStyle = TextStyle(DarkBrown)
        ),
        gridProperties = GridProperties(
            xAxisProperties = GridProperties.AxisProperties(
                color = SolidColor(MediumGrey),
            ),
            yAxisProperties = GridProperties.AxisProperties(
                color = SolidColor(MediumGrey),
            )
        ),
        indicatorProperties = HorizontalIndicatorProperties(padding = 8.dp, textStyle = TextStyle(DarkBrown)),
        labelHelperProperties = LabelHelperProperties(enabled = false),
        labelHelperPadding = 8.dp,
        popupProperties = PopupProperties(textStyle = TextStyle(Color.White)) { num ->
            "%.1f".format(num) + sensor.units
        }
    )
}

private fun chartLineBrush(
    sensor: Sensor,
    values: List<Double>,
    optimalValue: Double?
) = when {
    values.isEmpty() -> SolidColor(DarkGreen)
    optimalValue == null || values.size == 1 -> SolidColor(chartLatestStatusColor(sensor, values, optimalValue))
    else -> {
        val colorStops = values.mapIndexed { index, value ->
            val ratio = index.toFloat() / (values.lastIndex).coerceAtLeast(1)
            ratio to pointStatusColor(sensor, value, optimalValue)
        }.toTypedArray()

        Brush.horizontalGradient(colorStops = colorStops)
    }
}

private fun chartLatestStatusColor(
    sensor: Sensor,
    values: List<Double>,
    optimalValue: Double?
): Color {
    val latestValue = values.lastOrNull() ?: return DarkGreen
    return pointStatusColor(sensor, latestValue, optimalValue)
}

private fun pointStatusColor(
    sensor: Sensor,
    value: Double,
    optimalValue: Double?
): Color {
    return when (pointStatus(sensor, value, optimalValue)) {
        PointStatus.CLOSE -> DarkGreen
        PointStatus.MEDIUM -> DarkOrange
        PointStatus.FAR -> DarkRed
        PointStatus.NO_OPTIMAL -> SkyBlue
    }
}

private fun chartFillColor(
    sensor: Sensor,
    values: List<Double>,
    optimalValue: Double?
): Color {
    if (optimalValue == null) {
        return SkyBlue.copy(alpha = 0.5f)
    }

    val statuses = values.map { value -> pointStatus(sensor, value, optimalValue) }
    val greenCount = statuses.count { it == PointStatus.CLOSE }
    val redCount = statuses.count { it == PointStatus.FAR }

    return when {
        redCount > greenCount -> DarkRed.copy(alpha = 0.3f)
        greenCount > redCount -> DarkGreen.copy(alpha = 0.3f)
        else -> DarkOrange.copy(alpha = 0.3f)
    }
}

private fun pointStatus(
    sensor: Sensor,
    value: Double,
    optimalValue: Double?
): PointStatus {
    if (optimalValue == null) {
        return PointStatus.NO_OPTIMAL
    }

    val sensorRange = (sensor.maxValue - sensor.minValue).coerceAtLeast(1.0)
    val distanceRatio = abs(value - optimalValue) / sensorRange

    return when {
        distanceRatio <= 0.05 -> PointStatus.CLOSE
        distanceRatio <= 0.15 -> PointStatus.MEDIUM
        else -> PointStatus.FAR
    }
}

private enum class PointStatus {
    CLOSE,
    MEDIUM,
    FAR,
    NO_OPTIMAL
}