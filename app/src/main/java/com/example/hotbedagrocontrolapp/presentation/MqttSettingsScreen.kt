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
import com.example.hotbedagrocontrolapp.R
import com.example.hotbedagrocontrolapp.domain.viewModel.elements.AgroControlViewModel
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicBackArrow
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicConfirmButton
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicDropDownButton
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicTextField
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicTitle

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MqttSettingsScreen(
    viewModel: AgroControlViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    val mqttSettings = viewModel.mqttSettings

    val defaultIpAddress = mqttSettings.ipAddress.ifEmpty { stringResource(R.string.default_ip_address) }
    val defaultTopic = mqttSettings.mainTopic.ifEmpty { stringResource(R.string.default_topic) }
    val defaultUserName = mqttSettings.userName.ifEmpty { stringResource(R.string.default_user_name) }
    val defaultPassword = mqttSettings.password.ifEmpty { stringResource(R.string.default_password) }
    val defaultPort = mqttSettings.port.ifEmpty { stringResource(R.string.default_port) }

    var ipAddress by remember { mutableStateOf(defaultIpAddress) }
    var topic by remember { mutableStateOf(defaultTopic) }
    var userName by remember { mutableStateOf(defaultUserName) }
    var password by remember { mutableStateOf(defaultPassword) }
    var port by remember { mutableStateOf(defaultPort) }

    val context = LocalContext.current
    val parametersUpdatedMessage = stringResource(R.string.parameters_successfully_updated)

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
                text = stringResource(R.string.configuration_parameters),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        LazyColumn(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                BasicDropDownButton(
                    title = stringResource(R.string.what_is_it),
                    body = stringResource(R.string.what_is_mqtt),
                    modifier = Modifier.align(Alignment.Start)
                )
            }
            item {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.ip_address),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    BasicTextField(ipAddress) { newIpAddress ->
                        ipAddress = newIpAddress
                    }

                    Text(
                        text = stringResource(R.string.topic),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    BasicTextField(topic) { newTopic ->
                        topic = newTopic
                    }

                    Text(
                        text = stringResource(R.string.user_name),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    BasicTextField(userName) { newUserName ->
                        userName = newUserName
                    }

                    Text(
                        text = stringResource(R.string.password),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    BasicTextField(password) { newPassword ->
                        password = newPassword
                    }

                    Text(
                        text = stringResource(R.string.port),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary
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