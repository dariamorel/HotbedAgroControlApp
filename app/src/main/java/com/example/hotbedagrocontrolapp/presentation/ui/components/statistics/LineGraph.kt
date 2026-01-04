package com.example.hotbedagrocontrolapp.presentation.ui.components.statistics

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.hotbedagrocontrolapp.domain.entities.elements.Response
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.statistics.DateTime
import com.example.hotbedagrocontrolapp.ui.theme.DarkBrown
import com.example.hotbedagrocontrolapp.ui.theme.DarkGreen
import com.example.hotbedagrocontrolapp.ui.theme.MediumGrey
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties

const val STATISTICS_TAG = "Statistics"

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LineGraph(
    sensor: Sensor,
    values: List<Response>,
    labels: List<String>,
    modifier: Modifier = Modifier
) {
    LineChart(
        modifier = modifier
            .padding(bottom = 10.dp)
            .fillMaxSize(),
        data = remember(values, sensor) {
            listOf(
                Line(
                    label = sensor.elementName,
                    values = values.map { response ->
                        response.dataToDouble.coerceIn(sensor.minValue, sensor.maxValue)
                    },
                    color = SolidColor(Color(0xFF23af92)),
                    firstGradientFillColor = Color(0xFF2BC0A1).copy(alpha = .5f),
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