package com.example.hotbedagrocontrolapp.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hotbedagrocontrolapp.R
import com.example.hotbedagrocontrolapp.domain.viewModel.elements.AgroControlViewModel
import com.example.hotbedagrocontrolapp.presentation.components.BasicConfirmButton
import com.example.hotbedagrocontrolapp.presentation.components.BasicOpenButton
import com.example.hotbedagrocontrolapp.presentation.components.BasicTitle

/**
 * Экран с устройствами.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DevicesScreen(
    navController: NavController,
    viewModel: AgroControlViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        BasicTitle("Управление устройством")
        Spacer(Modifier.size(8.dp))
        BasicOpenButton(
            text = "Параметры конфигурации",
        ) {
            navController.navigate(Screens.MQTT_SETTINGS.title)
        }
        BasicOpenButton(
            text = "Оптимальные значения датчиков",
        )
        BasicConfirmButton(
            text = stringResource(R.string.delete_device),
            color = Color.Red
        ) {
            viewModel.deleteDevice()
        }
    }
}