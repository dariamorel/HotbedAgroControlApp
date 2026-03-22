package com.example.hotbedagrocontrolapp.presentation.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotbedagrocontrolapp.domain.entities.elements.Control
import com.example.hotbedagrocontrolapp.domain.entities.elements.ControlResponse
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.statistics.DateTime
import com.example.hotbedagrocontrolapp.presentation.ui.components.eventLog.Event
import com.example.hotbedagrocontrolapp.presentation.ui.components.statistics.SwitchDateTime
import com.example.hotbedagrocontrolapp.presentation.ui.components.statistics.SwitchElement
import com.example.hotbedagrocontrolapp.presentation.viewModel.statistics.StatisticsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventLogScreen(
    viewModel: StatisticsViewModel,
    modifier: Modifier = Modifier
) {
    var control by remember { mutableStateOf(Control.CLEAR_CLOUDY) }
    var dateTime by remember { mutableStateOf(DateTime()) }
    val values by viewModel.getDataHistory(control, dateTime).collectAsState()

    Column(
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SwitchElement(
                element = control,
                options = Control.entries,
                modifier = Modifier.weight(1f)
            ) { selected ->
                control = selected as Control
                viewModel.updateDataBase(control, dateTime)
            }
            SwitchDateTime(dateTime) { newDateTime ->
                dateTime = newDateTime
                viewModel.updateDataBase(control, dateTime)
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            values.forEach { (localDateTime, response) ->
                item {
                    Event(control, localDateTime, response)
                }
            }
        }
    }
}