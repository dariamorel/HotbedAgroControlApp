package com.example.hotbedagrocontrolapp.presentation.ui.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
    val by = HistoryBy.DAY
    val dateTime = LocalDateTime.of(2025, 12, 14, 0, 0)
    val values by viewModel.getDataHistory(element, by, dateTime).collectAsState()
    LineGraph(Sensor.AIR_TEMPERATURE, values)
}