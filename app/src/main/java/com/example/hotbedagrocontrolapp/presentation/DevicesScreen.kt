package com.example.hotbedagrocontrolapp.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hotbedagrocontrolapp.presentation.components.devices.AddDeviceScreen
import com.example.hotbedagrocontrolapp.presentation.components.devices.DeleteDeviceScreen
import com.example.hotbedagrocontrolapp.domain.viewModel.elements.AgroControlViewModel
import com.example.hotbedagrocontrolapp.presentation.components.BasicOpenButton

/**
 * Экран с устройствами.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DevicesScreen(
    viewModel: AgroControlViewModel,
    modifier: Modifier = Modifier
) {
    BasicOpenButton(
        text = "Hello",
        modifier = modifier.padding(10.dp)
    )
}