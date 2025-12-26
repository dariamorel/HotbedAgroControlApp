package com.example.hotbedagrocontrolapp.presentation.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hotbedagrocontrolapp.domain.entities.statistics.AnaliseType
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.statistics.DateTime
import com.example.hotbedagrocontrolapp.presentation.ui.components.statistics.LineGraph
import com.example.hotbedagrocontrolapp.presentation.ui.components.statistics.SwitchAnaliseType
import com.example.hotbedagrocontrolapp.presentation.ui.components.statistics.SwitchDateTime
import com.example.hotbedagrocontrolapp.presentation.ui.components.statistics.SwitchSensor
import com.example.hotbedagrocontrolapp.presentation.viewModel.statistics.StatisticsViewModel
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatisticsGraphScreen(
    viewModel: StatisticsViewModel,
    modifier: Modifier = Modifier
) {
    var sensor by remember { mutableStateOf(Sensor.AIR_TEMPERATURE) }
    var analiseType by remember { mutableStateOf(AnaliseType.DAY) }
    var dateTime by remember { mutableStateOf(DateTime(analiseType)) }
    val values by viewModel.getDataHistory(sensor, dateTime).collectAsState()

    Column(
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SwitchSensor(Modifier.weight(2f)) { selected ->
                sensor = selected
            }

            SwitchAnaliseType(Modifier.weight(1f)) { selected ->
                analiseType = selected
                dateTime = DateTime(analiseType)
            }
        }

        SwitchDateTime(dateTime, Modifier.align(Alignment.End)) { newDateTime ->
            dateTime = newDateTime
        }

        LineGraph(Sensor.AIR_TEMPERATURE, values)
    }
}
