package com.example.hotbedagrocontrolapp.presentation.ui.components

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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotbedagrocontrolapp.domain.entities.Control
import com.example.hotbedagrocontrolapp.domain.entities.ControlResponse

@Composable
fun ControlComponent(
    control: Control,
    response: ControlResponse,
    modifier: Modifier = Modifier,
    onStatusChanged: (Boolean) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
            .clip(RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
                .align(Alignment.Center),
        ) {
            Row{
                Icon(
                    painter = painterResource(control.iconInfo.resourceId),
                    contentDescription = "Иконка ${control.elementName}",
                    modifier = Modifier.size(35.dp).align(Alignment.Top),
                    tint = control.iconInfo.tint
                )
                Box(
                    modifier = Modifier.fillMaxWidth().padding(5.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = control.elementName,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Switch(
                    checked = response.data == ControlResponse.Status.ON,
                    onCheckedChange = onStatusChanged
                )
            }
        }
    }
}