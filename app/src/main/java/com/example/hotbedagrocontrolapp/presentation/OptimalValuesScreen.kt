package com.example.hotbedagrocontrolapp.presentation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hotbedagrocontrolapp.R
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.viewModel.elements.AgroControlViewModel
import com.example.hotbedagrocontrolapp.presentation.components.ai.FixInChat
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicBackArrow
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicConfirmButton
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicDropDownButton
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicTextField
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicTitle
import com.example.hotbedagrocontrolapp.ui.theme.DarkBlue
import com.example.hotbedagrocontrolapp.ui.theme.LightBlue
import com.example.hotbedagrocontrolapp.ui.theme.SkyBlue

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OptimalValuesScreen(
    viewModel: AgroControlViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val optimalValues by viewModel.optimalValues.collectAsState()
    val parametersUpdatedMessage = stringResource(R.string.optimal_values_successfully_updated)

    var airHumidityOpt by remember { mutableStateOf("") }
    var airTemperatureOpt by remember { mutableStateOf("") }
    var fluidTemperatureOpt by remember { mutableStateOf("") }
    var fluidLevelOpt by remember { mutableStateOf("") }
    var ecOpt by remember { mutableStateOf("") }
    var luxOpt by remember { mutableStateOf("") }
    var phOpt by remember { mutableStateOf("") }

    LaunchedEffect(optimalValues) {
        airHumidityOpt = optimalValues[Sensor.AIR_HUMIDITY]?.toString().orEmpty()
        airTemperatureOpt = optimalValues[Sensor.AIR_TEMPERATURE]?.toString().orEmpty()
        fluidTemperatureOpt = optimalValues[Sensor.FLUID_TEMPERATURE]?.toString().orEmpty()
        fluidLevelOpt = optimalValues[Sensor.FLUID_LEVEL]?.toString().orEmpty()
        ecOpt = optimalValues[Sensor.EC]?.toString().orEmpty()
        luxOpt = optimalValues[Sensor.LUX]?.toString().orEmpty()
        phOpt = optimalValues[Sensor.PH]?.toString().orEmpty()
    }

    val messageForAi = stringResource(R.string.choose_optimal_values_ai_message)
    val backScreen = Screens.OPTIMAL_VALUES.title

    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BasicBackArrow(Modifier) { onBack() }
            Text(
                text = stringResource(R.string.optimal_values),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                BasicDropDownButton(
                    title = stringResource(R.string.what_is_it),
                    body = stringResource(R.string.what_is_optimal_values),
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            item {
                FixInChat(
                    text = stringResource(R.string.choose_optimal_values_in_chat),
                    modifier = Modifier.align(Alignment.Start),
                ) {
                    navController.navigate("${Screens.AI_CHAT.title}/$messageForAi/$backScreen")
                }
            }

            item {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.air_humidity_opt),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Left,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    BasicTextField(airHumidityOpt) { newAirHumidityOpt ->
                        airHumidityOpt = newAirHumidityOpt
                    }

                    Text(
                        text = stringResource(R.string.air_temperature_opt),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Left,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    BasicTextField(airTemperatureOpt) { newAirTemperatureOpt ->
                        airTemperatureOpt = newAirTemperatureOpt
                    }

                    Text(
                        text = stringResource(R.string.fluid_temperature_opt),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Left,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    BasicTextField(fluidTemperatureOpt) { newFluidTemperatureOpt ->
                        fluidTemperatureOpt = newFluidTemperatureOpt
                    }

                    Text(
                        text = stringResource(R.string.fluid_level_opt),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Left,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    BasicTextField(fluidLevelOpt) { newFluidLevelOpt ->
                        fluidLevelOpt = newFluidLevelOpt
                    }

                    Text(
                        text = stringResource(R.string.ec_opt),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Left,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    BasicTextField(ecOpt) { newEcOpt ->
                        ecOpt = newEcOpt
                    }

                    Text(
                        text = stringResource(R.string.lux_opt),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Left,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    BasicTextField(luxOpt) { newLuxOpt ->
                        luxOpt = newLuxOpt
                    }

                    Text(
                        text = stringResource(R.string.ph_opt),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Left,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    BasicTextField(phOpt) { newPhOpt ->
                        phOpt = newPhOpt
                    }
                }
            }

        }
        BasicConfirmButton(
            text = stringResource(R.string.update_parameters),
        ) {
            viewModel.saveOptimalValues(
                mapOf(
                    Sensor.AIR_HUMIDITY to airHumidityOpt,
                    Sensor.AIR_TEMPERATURE to airTemperatureOpt,
                    Sensor.FLUID_TEMPERATURE to fluidTemperatureOpt,
                    Sensor.FLUID_LEVEL to fluidLevelOpt,
                    Sensor.EC to ecOpt,
                    Sensor.LUX to luxOpt,
                    Sensor.PH to phOpt
                )
            )
            Toast.makeText(context, parametersUpdatedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}