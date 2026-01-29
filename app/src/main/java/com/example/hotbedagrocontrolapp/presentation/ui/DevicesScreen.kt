package com.example.hotbedagrocontrolapp.presentation.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.hotbedagrocontrolapp.presentation.ui.components.devices.AddDeviceScreen
import com.example.hotbedagrocontrolapp.presentation.ui.components.devices.DeleteDeviceScreen
import com.example.hotbedagrocontrolapp.presentation.viewModel.elements.AgroControlViewModel

/**
 * Экран с устройствами.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DevicesScreen(
    viewModel: AgroControlViewModel,
    modifier: Modifier = Modifier
) {
    val isDeviceAdded by viewModel.isDeviceAdded.collectAsState()

    if (isDeviceAdded) {
        DeleteDeviceScreen(viewModel, modifier)
    } else {
        AddDeviceScreen(viewModel, modifier)
    }

}