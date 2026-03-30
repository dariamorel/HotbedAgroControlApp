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
import com.example.hotbedagrocontrolapp.domain.viewModel.elements.AgroControlViewModel
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicConfirmButton
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicDropDownButton
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicTextField
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicTitle

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MqttSettingsScreen(
    viewModel: AgroControlViewModel,
    modifier: Modifier = Modifier
) {
    val mqttSettings = viewModel.mqttSettings

    val default_ip_address = mqttSettings.ipAddress.ifEmpty { stringResource(R.string.default_ip_address) }
    val default_topic = mqttSettings.mainTopic.ifEmpty { stringResource(R.string.default_topic) }
    val default_user_name = mqttSettings.userName.ifEmpty { stringResource(R.string.default_user_name) }
    val default_password = mqttSettings.password.ifEmpty { stringResource(R.string.default_password) }
    val default_port = mqttSettings.port.ifEmpty { stringResource(R.string.default_port) }

    var ipAddress by remember { mutableStateOf(default_ip_address) }
    var topic by remember { mutableStateOf(default_topic) }
    var userName by remember { mutableStateOf(default_user_name) }
    var password by remember { mutableStateOf(default_password) }
    var port by remember { mutableStateOf(default_port) }

    val context = LocalContext.current
    val parametersUpdatedMessage = stringResource(R.string.parameters_successfully_updated)

    Column(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        LazyColumn(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                BasicTitle(
                    text = stringResource(R.string.configuration_parameters),
                )
            }
            item {
                BasicDropDownButton(
                    title = stringResource(R.string.what_is_it),
                    body = stringResource(R.string.what_is_mqtt)
                )
            }
            item {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BasicTitle(
                        text = stringResource(R.string.ip_address),
                        fontSize = 20,
                    )
                    BasicTextField(ipAddress) { newIpAddress ->
                        ipAddress = newIpAddress
                    }

                    BasicTitle(
                        text = stringResource(R.string.topic),
                        fontSize = 20,
                    )
                    BasicTextField(topic) { newTopic ->
                        topic = newTopic
                    }

                    BasicTitle(
                        text = stringResource(R.string.user_name),
                        fontSize = 20,
                    )
                    BasicTextField(userName) { newUserName ->
                        userName = newUserName
                    }

                    BasicTitle(
                        text = stringResource(R.string.password),
                        fontSize = 20,
                    )
                    BasicTextField(password) { newPassword ->
                        password = newPassword
                    }

                    BasicTitle(
                        text = stringResource(R.string.port),
                        fontSize = 20,
                    )
                    BasicTextField(port) { newPort ->
                        port = newPort
                    }
                }
            }
        }
        BasicConfirmButton(
            text = stringResource(R.string.update_parameters),
        ) {
            viewModel.addDevice(ipAddress, topic, userName, password, port)
            Toast.makeText(context, parametersUpdatedMessage, Toast.LENGTH_SHORT).show()
        }
    }

}