package com.example.hotbedagrocontrolapp.presentation.components.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.elements.SensorResponse
import com.example.hotbedagrocontrolapp.ui.theme.DarkBrown
import com.example.hotbedagrocontrolapp.ui.theme.DarkGreen
import com.example.hotbedagrocontrolapp.ui.theme.DarkRed
import com.example.hotbedagrocontrolapp.ui.theme.DarkYellow
import com.example.hotbedagrocontrolapp.ui.theme.LightGreen
import com.example.hotbedagrocontrolapp.ui.theme.LightRed
import com.example.hotbedagrocontrolapp.ui.theme.LightYellow
import kotlin.math.abs

/**
 * Отображение датчика.
 *
 * @param sensor Датчик.
 * @param response Значение датчика, полученное с устройства.
 */
@Composable
fun SensorComponent(
    sensor: Sensor,
    response: SensorResponse,
    modifier: Modifier = Modifier,
    optimalValue: Double? = null,
    hasActualData: Boolean = true
) {
    val (backgroundColor, valueColor) = sensorStatusColors(
        sensor = sensor,
        actualValue = response.data,
        optimalValue = optimalValue,
        hasActualData = hasActualData,
        defaultBackground = MaterialTheme.colorScheme.surface,
        defaultValueColor = MaterialTheme.colorScheme.onSurface
    )
    val density = LocalDensity.current
    val iconSize = with(density) { MaterialTheme.typography.titleSmall.fontSize.toDp() * 2 }

    Box(
        modifier = modifier.fillMaxSize()
            .clip(RoundedCornerShape(30.dp))
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
                .align(Alignment.Center)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(sensor.iconInfo.resourceId),
                    contentDescription = "Иконка ${sensor.elementName}",
                    modifier = Modifier.size(iconSize).align(Alignment.Top),
                    tint = sensor.iconInfo.tint
                )
                Box(
                    modifier = Modifier.fillMaxWidth().padding(5.dp),
//                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = sensor.elementName,
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "${response.data}",
                    fontSize = 30.sp,
                    style = MaterialTheme.typography.bodyLarge,
                    color = valueColor
                )
            }
        }
    }
}

private fun sensorStatusColors(
    sensor: Sensor,
    actualValue: Double,
    optimalValue: Double?,
    hasActualData: Boolean,
    defaultBackground: Color,
    defaultValueColor: Color
): Pair<Color, Color> {
    if (!hasActualData || optimalValue == null) {
        return defaultBackground to defaultValueColor
    }

    val sensorRange = (sensor.maxValue - sensor.minValue).coerceAtLeast(1.0)
    val distanceRatio = abs(actualValue - optimalValue) / sensorRange

    return when {
        distanceRatio <= 0.05 -> defaultBackground to DarkGreen
        distanceRatio <= 0.15 -> defaultBackground to DarkYellow
        else -> LightRed to DarkRed
    }
}