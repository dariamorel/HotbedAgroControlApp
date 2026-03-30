package com.example.hotbedagrocontrolapp.presentation.components.elements

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hotbedagrocontrolapp.R
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.viewModel.elements.AgroControlViewModel
import com.example.hotbedagrocontrolapp.presentation.Screens
import com.example.hotbedagrocontrolapp.presentation.components.ai.FixInChat
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicBodyText
import com.example.hotbedagrocontrolapp.presentation.components.statistics.PointStatus
import com.example.hotbedagrocontrolapp.presentation.components.statistics.pointStatus
import com.example.hotbedagrocontrolapp.ui.theme.DarkBlue
import com.example.hotbedagrocontrolapp.ui.theme.DarkOrange
import com.example.hotbedagrocontrolapp.ui.theme.DarkRed
import com.example.hotbedagrocontrolapp.ui.theme.DarkYellow
import com.example.hotbedagrocontrolapp.ui.theme.LightRed
import com.example.hotbedagrocontrolapp.ui.theme.SunYellow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WarningComponent(
    viewModel: AgroControlViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentData by viewModel.currentData.collectAsState()
    val optimalValues by viewModel.optimalValues.collectAsState()
    val sensors = currentData.filter { (entity, _) ->
        entity is Sensor
    }
    val sensorsWithRedValues = sensors.filter { (sensor, response) ->
        pointStatus(sensor as Sensor, response.dataToDouble, optimalValues[sensor]) == PointStatus.FAR
    }

    if (sensorsWithRedValues.isEmpty()) return

    val sensorsWithRedValuesList = sensorsWithRedValues.toList()
    val message = stringResource(
        R.string.values_in_red_zone,
        sensorsWithRedValuesList.joinToString("\n- ") { it.first.elementName }
    )
    val messageForAi = stringResource(
        R.string.values_in_red_zone_ai_message,
        sensorsWithRedValuesList.joinToString(", ") { it.first.elementName }
    )
    val backScreen = Screens.ELEMENTS.title

    Box(
        modifier = modifier.fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(LightRed)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = DarkOrange,
                    modifier = modifier.size(20.dp)
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            FixInChat(
                text = stringResource(R.string.fix_in_chat),
                modifier = Modifier.align(Alignment.End)
            ) {
                navController.navigate("${Screens.AI_CHAT.title}/$messageForAi/$backScreen")
            }
        }
    }
}