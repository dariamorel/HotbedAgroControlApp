package com.example.hotbedagrocontrolapp.presentation

import android.os.Build
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hotbedagrocontrolapp.R
import com.example.hotbedagrocontrolapp.domain.viewModel.elements.AgroControlViewModel
import com.example.hotbedagrocontrolapp.presentation.components.BasicConfirmButton
import com.example.hotbedagrocontrolapp.presentation.components.BasicOpenButton
import com.example.hotbedagrocontrolapp.presentation.components.BasicTitle
import com.example.hotbedagrocontrolapp.ui.theme.DarkRed

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
    val context = LocalContext.current
    val deviceDeletedMessage = stringResource(R.string.device_successfully_deleted)

    val isDeviceAdded by viewModel.isDeviceAdded.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        BasicTitle(stringResource(R.string.device_management))
        Spacer(Modifier.size(8.dp))
        BasicOpenButton(
            text = stringResource(R.string.configuration_parameters),
        ) {
            navController.navigate(Screens.MQTT_SETTINGS.title)
        }
        BasicOpenButton(
            text = stringResource(R.string.optimal_values),
        ) {
            navController.navigate(Screens.OPTIMAL_VALUES.title)
        }
        if (isDeviceAdded) {
            BasicConfirmButton(
                text = stringResource(R.string.delete_device),
                color = DarkRed
            ) {
                viewModel.deleteDevice()
                Toast.makeText(context, deviceDeletedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}