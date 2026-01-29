package com.example.hotbedagrocontrolapp.presentation.ui.components.devices

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotbedagrocontrolapp.presentation.viewModel.elements.AgroControlViewModel

/**
 * Экран для удаления устройства.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeleteDeviceScreen(
    viewModel: AgroControlViewModel,
    modifier: Modifier = Modifier
) {
    val mqttSettings = viewModel.mqttSettings

    Column(modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "IP адрес: ${mqttSettings.ipAddress}",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        )
        Text(
            text = "Топик: ${mqttSettings.mainTopic}",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        )

        Text(
            text = "Имя пользователя: ${mqttSettings.userName}",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        )

        Text(
            text = "Пароль: ${mqttSettings.password}",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        )


        Button(
            onClick = { viewModel.deleteDevice() },
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = "Удалить устройство",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}