package com.example.hotbedagrocontrolapp.presentation.ui.components.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.elements.SensorResponse

@Composable
fun SensorComponent(
    sensor: Sensor,
    response: SensorResponse,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
            .clip(RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
                .align(Alignment.Center)
        ) {
            Row{
                Icon(
                    painter = painterResource(sensor.iconInfo.resourceId),
                    contentDescription = "Иконка ${sensor.elementName}",
                    modifier = Modifier.size(35.dp).align(Alignment.Top),
                    tint = sensor.iconInfo.tint
                )
                Box(
                    modifier = Modifier.fillMaxWidth().padding(5.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = sensor.elementName,
                        style = MaterialTheme.typography.titleMedium,
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
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}