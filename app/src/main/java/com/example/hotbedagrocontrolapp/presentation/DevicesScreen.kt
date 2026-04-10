package com.example.hotbedagrocontrolapp.presentation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hotbedagrocontrolapp.R
import com.example.hotbedagrocontrolapp.domain.viewModel.elements.AgroControlViewModel
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicBodyText
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicConfirmButton
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicOpenButton
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicTitle
import com.example.hotbedagrocontrolapp.ui.theme.DarkRed
import com.example.hotbedagrocontrolapp.ui.theme.LightGreen

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
    val backScreen = Screens.DEVICES.title

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        BasicOpenButton(
            text = stringResource(R.string.configuration_parameters),
        ) {
            navController.navigate("${Screens.MQTT_SETTINGS.title}/$backScreen")
        }
        BasicOpenButton(
            text = stringResource(R.string.optimal_values),
        ) {
            navController.navigate("${Screens.OPTIMAL_VALUES.title}/$backScreen")
        }
    }
}