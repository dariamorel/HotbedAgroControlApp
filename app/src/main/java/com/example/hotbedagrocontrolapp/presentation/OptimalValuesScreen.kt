package com.example.hotbedagrocontrolapp.presentation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import com.example.hotbedagrocontrolapp.R
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.viewModel.elements.AgroControlViewModel
import com.example.hotbedagrocontrolapp.presentation.components.BasicConfirmButton
import com.example.hotbedagrocontrolapp.presentation.components.BasicDropDownButton
import com.example.hotbedagrocontrolapp.presentation.components.BasicTextField
import com.example.hotbedagrocontrolapp.presentation.components.BasicTitle

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OptimalValuesScreen(
    viewModel: AgroControlViewModel,
    modifier: Modifier = Modifier
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

    LazyColumn(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            BasicTitle(
                text = stringResource(R.string.optimal_values),
            )
        }
        item {
            BasicDropDownButton(
                title = stringResource(R.string.what_is_it),
                body = stringResource(R.string.what_is_optimal_values)
            )
        }
        item {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BasicTitle(
                    text = stringResource(R.string.air_humidity_opt),
                    fontSize = 20,
                )
                BasicTextField(airHumidityOpt) { newAirHumidityOpt ->
                    airHumidityOpt = newAirHumidityOpt
                }

                BasicTitle(
                    text = stringResource(R.string.air_temperature_opt),
                    fontSize = 20,
                )
                BasicTextField(airTemperatureOpt) { newAirTemperatureOpt ->
                    airTemperatureOpt = newAirTemperatureOpt
                }

                BasicTitle(
                    text = stringResource(R.string.fluid_temperature_opt),
                    fontSize = 20,
                )
                BasicTextField(fluidTemperatureOpt) { newFluidTemperatureOpt ->
                    fluidTemperatureOpt = newFluidTemperatureOpt
                }

                BasicTitle(
                    text = stringResource(R.string.fluid_level_opt),
                    fontSize = 20,
                )
                BasicTextField(fluidLevelOpt) { newFluidLevelOpt ->
                    fluidLevelOpt = newFluidLevelOpt
                }

                BasicTitle(
                    text = stringResource(R.string.ec_opt),
                    fontSize = 20,
                )
                BasicTextField(ecOpt) { newEcOpt ->
                    ecOpt = newEcOpt
                }

                BasicTitle(
                    text = stringResource(R.string.lux_opt),
                    fontSize = 20,
                )
                BasicTextField(luxOpt) { newLuxOpt ->
                    luxOpt = newLuxOpt
                }

                BasicTitle(
                    text = stringResource(R.string.ph_opt),
                    fontSize = 20,
                )
                BasicTextField(phOpt) { newPhOpt ->
                    phOpt = newPhOpt
                }
            }
        }
        item {
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

}