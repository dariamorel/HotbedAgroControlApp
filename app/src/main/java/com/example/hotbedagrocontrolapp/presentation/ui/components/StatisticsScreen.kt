package com.example.hotbedagrocontrolapp.presentation.ui.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.hotbedagrocontrolapp.domain.entities.HistoryBy
import com.example.hotbedagrocontrolapp.domain.entities.Sensor
import com.example.hotbedagrocontrolapp.presentation.viewModel.AgroControlViewModel
import java.time.LocalDateTime
import java.time.Month

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsScreen(
    viewModel: AgroControlViewModel,
    modifier: Modifier = Modifier
) {
    val element = Sensor.AIR_TEMPERATURE
    val by = HistoryBy.HOUR
    val dateTime = setCorrectDateTime(2025, 12, 15, 0)
    val values by viewModel.getDataHistory(element, by, dateTime).collectAsState()

    LineGraph(Sensor.AIR_TEMPERATURE, values)
}

@RequiresApi(Build.VERSION_CODES.O)
fun setCorrectDateTime(
    year: Int,
    month: Int = 1,
    day: Int = 1,
    hour: Int = 0,
    minute: Int = 0
): LocalDateTime = LocalDateTime.of(year, month, day, hour, minute)