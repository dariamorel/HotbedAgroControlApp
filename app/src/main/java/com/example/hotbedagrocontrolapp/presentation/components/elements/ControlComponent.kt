package com.example.hotbedagrocontrolapp.presentation.components.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hotbedagrocontrolapp.domain.entities.elements.Control
import com.example.hotbedagrocontrolapp.domain.entities.elements.ControlResponse
import com.example.hotbedagrocontrolapp.ui.theme.DarkBrown
import com.example.hotbedagrocontrolapp.ui.theme.DarkGreen
import com.example.hotbedagrocontrolapp.ui.theme.LightGreen

/**
 * Отображение элемента управления.
 *
 * @param control Элемент управления.
 * @param response Статус элемента управления, полученное с устройства.
 * @param onStatusChanged Обработка изменения статуса элемента управления.
 */
@Composable
fun ControlComponent(
    control: Control,
    response: ControlResponse,
    modifier: Modifier = Modifier,
    onStatusChanged: (Boolean) -> Unit
) {
    val density = LocalDensity.current
    val iconSize = with(density) { MaterialTheme.typography.titleSmall.fontSize.toDp() * 2 }

    Box(
        modifier = modifier.fillMaxSize()
            .clip(RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
                .align(Alignment.Center),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(control.iconInfo.resourceId),
                    contentDescription = "Иконка ${control.elementName}",
                    modifier = Modifier.size(iconSize).align(Alignment.Top),
                    tint = control.iconInfo.tint
                )
                Box(
                    modifier = Modifier.fillMaxWidth().padding(5.dp),
//                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = control.elementName,
                        style = MaterialTheme.typography.titleSmall,
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
                    onCheckedChange = onStatusChanged,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = DarkGreen,
                        uncheckedTrackColor = LightGreen,
                        checkedThumbColor = DarkBrown,
                    )
                )
            }
        }
    }
}