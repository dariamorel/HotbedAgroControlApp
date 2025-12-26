package com.example.hotbedagrocontrolapp.presentation.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotbedagrocontrolapp.domain.entities.elements.Control
import com.example.hotbedagrocontrolapp.domain.entities.elements.ControlResponse
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.elements.SensorResponse
import com.example.hotbedagrocontrolapp.presentation.ui.components.elements.ControlComponent
import com.example.hotbedagrocontrolapp.presentation.ui.components.elements.SensorComponent
import com.example.hotbedagrocontrolapp.presentation.viewModel.elements.AgroControlViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ElementsScreen(
    viewModel: AgroControlViewModel,
    modifier: Modifier = Modifier
) {
    val currentData by viewModel.currentData.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Датчики",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 25.sp,
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        for (i in 0 until Sensor.entries.size step 2) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val sensor = Sensor.entries[i]
                    currentData[sensor]?.let { response ->
                        SensorComponent(sensor, response as SensorResponse, Modifier.weight(1f).aspectRatio(1f))
                    }
                    if (i + 1 < Sensor.entries.size) {
                        val sensor = Sensor.entries[i + 1]
                        currentData[sensor]?.let { response ->
                            SensorComponent(sensor, response as SensorResponse, Modifier.weight(1f).aspectRatio(1f))
                        }
                    } else {
                        Spacer(Modifier.weight(1f).aspectRatio(1f))
                    }
                }
            }
        }
        item {
            Text(
                text = "Элементы управления",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 25.sp,
                textAlign = TextAlign.Left,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        for (i in 0 until Control.entries.size step 2) {
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val control = Control.entries[i]
                    currentData[control]?.let { response ->
                        ControlComponent(control, response as ControlResponse, Modifier.weight(1f).aspectRatio(1f)) { isControlOn ->
                            viewModel.onStatusChanged(control, isControlOn)

                        }
                    }
                    if (i + 1 < Control.entries.size) {
                        val control = Control.entries[i + 1]
                        currentData[control]?.let { response ->
                            ControlComponent(control, response as ControlResponse, Modifier.weight(1f).aspectRatio(1f)) { isControlOn ->
                                viewModel.onStatusChanged(control, isControlOn)
                            }
                        }
                    } else {
                        Spacer(Modifier.weight(1f).aspectRatio(1f))
                    }
                }
            }
        }
    }
}