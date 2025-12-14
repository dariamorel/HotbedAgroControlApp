package com.example.hotbedagrocontrolapp.presentation.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hotbedagrocontrolapp.domain.entities.Sensor
import com.example.hotbedagrocontrolapp.presentation.viewModel.AgroControlViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsScreen(
    viewModel: AgroControlViewModel,
    modifier: Modifier = Modifier
) {
    LineGraph(Sensor.AIR_TEMPERATURE, viewModel)
}