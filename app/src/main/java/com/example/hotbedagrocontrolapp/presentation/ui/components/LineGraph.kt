package com.example.hotbedagrocontrolapp.presentation.ui.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.example.hotbedagrocontrolapp.domain.entities.Sensor
import com.example.hotbedagrocontrolapp.presentation.viewModel.AgroControlViewModel
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LineGraph(
    sensor: Sensor,
    viewModel: AgroControlViewModel,
    modifier: Modifier = Modifier
) {
    val values by viewModel.getDataHistory(sensor).collectAsState()

    LineChart(
        modifier = modifier.fillMaxSize().padding(horizontal = 22.dp),
            data = remember(values) {
                listOf(
                    Line(
                        label = sensor.topic,
                        values = values.map { (time, response) ->
                            response.dataToDouble
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
            labels = values.map { (time, _) ->
                time.format(DateTimeFormatter.ofPattern("hh:mm"))
            }
        )
    )
}