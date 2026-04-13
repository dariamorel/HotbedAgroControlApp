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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotbedagrocontrolapp.R
import com.example.hotbedagrocontrolapp.domain.viewModel.elements.AgroControlViewModel
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicBackArrow
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicConfirmButton
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicDropDownButton
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicTextField
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicTitle
import com.example.hotbedagrocontrolapp.ui.theme.DarkBlue
import com.example.hotbedagrocontrolapp.ui.theme.DarkGreen
import com.example.hotbedagrocontrolapp.ui.theme.DarkRed

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MqttSettingsScreen(
    viewModel: AgroControlViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    val mqttSettings = viewModel.mqttSettings

    val defaultIpAddress = stringResource(R.string.default_ip_address)
    val defaultTopic = stringResource(R.string.default_topic)
    val defaultUserName = stringResource(R.string.default_user_name)
    val defaultPassword = stringResource(R.string.default_password)
    val defaultPort = stringResource(R.string.default_port)

    var ipAddress by remember { mutableStateOf(mqttSettings.ipAddress) }
    var topic by remember { mutableStateOf(mqttSettings.mainTopic) }
    var userName by remember { mutableStateOf(mqttSettings.userName) }
    var password by remember { mutableStateOf(mqttSettings.password) }
    var port by remember { mutableStateOf(mqttSettings.port) }

    var showIpAddressError by remember { mutableStateOf(false) }
    var showTopicError by remember { mutableStateOf(false) }
    var showUserNameError by remember { mutableStateOf(false) }
    var showPasswordError by remember { mutableStateOf(false) }
    var showPortError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val parametersUpdatedMessage = stringResource(R.string.parameters_successfully_updated)
    val connectionErrorMessage = stringResource(R.string.connection_error)

    val deviceDeletedMessage = stringResource(R.string.device_successfully_deleted)

    var isCheckBoxOpened by remember { mutableStateOf(false) }
    val connectionError by viewModel.connectionError.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()
    val isDeviceAdded by viewModel.isDeviceAdded.collectAsState()
    var showToast by remember { mutableStateOf(false) }

    val readOnly = isDeviceAdded
    val density = LocalDensity.current
    val iconSize = with(density) { MaterialTheme.typography.bodySmall.fontSize.toDp() }

    LaunchedEffect(connectionError) {
        if (connectionError && showToast) {
            Toast.makeText(context, connectionErrorMessage, Toast.LENGTH_SHORT).show()
            viewModel.removeConnectionError()
            showIpAddressError = true
            showTopicError = true
            showUserNameError = true
            showPasswordError = true
            showPortError = true
        }
    }

    LaunchedEffect(isConnected) {
        if (isConnected && showToast) {
            Toast.makeText(context, parametersUpdatedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            BasicBackArrow(Modifier) { onBack() }
            Spacer(Modifier.size(4.dp))
            Text(
                text = stringResource(R.string.configuration_parameters),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
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
                if (isDeviceAdded) {
                    Text(
                        text = "✅ " + stringResource(R.string.connection_got),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                }
            }
            item {
                OutlinedTextField(
                    value = ipAddress,
                    textStyle = MaterialTheme.typography.bodySmall,
                    onValueChange = { newIpAddress ->
                        ipAddress = newIpAddress
                        showIpAddressError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text(
                        text = stringResource(R.string.ip_address) + '*',
                        style = MaterialTheme.typography.bodySmall
                    ) },
                    isError = showIpAddressError,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    placeholder = {
                        Text(
                            text = defaultIpAddress,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    readOnly = readOnly
                )
            }
            item {
                OutlinedTextField(
                    value = topic,
                    textStyle = MaterialTheme.typography.bodySmall,
                    onValueChange = { newTopic ->
                        topic = newTopic
                        showTopicError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text(
                        text = stringResource(R.string.topic) + '*',
                        style = MaterialTheme.typography.bodySmall
                    ) },
                    isError = showTopicError,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    placeholder = {
                        Text(
                            text = defaultTopic,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    readOnly = readOnly
                )
            }
            item {
                OutlinedTextField(
                    value = userName,
                    textStyle = MaterialTheme.typography.bodySmall,
                    onValueChange = { newUserName ->
                        userName = newUserName
                        showUserNameError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text(
                        text = stringResource(R.string.user_name) + '*',
                        style = MaterialTheme.typography.bodySmall
                    ) },
                    isError = showUserNameError,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    placeholder = {
                        Text(
                            text = defaultUserName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    readOnly = readOnly
                )
            }
            item {
                OutlinedTextField(
                    value = password,
                    textStyle = MaterialTheme.typography.bodySmall,
                    onValueChange = { newPassword ->
                        password = newPassword
                        showPasswordError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text(
                        text = stringResource(R.string.password) + '*',
                        style = MaterialTheme.typography.bodySmall
                    ) },
                    isError = showPasswordError,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    placeholder = {
                        Text(
                            text = defaultPassword,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    readOnly = readOnly,
                )
            }
            item {
                OutlinedTextField(
                    value = port,
                    textStyle = MaterialTheme.typography.bodySmall,
                    onValueChange = { newPort ->
                        port = newPort
                        showPortError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text(
                        text = stringResource(R.string.port) + '*',
                        style = MaterialTheme.typography.bodySmall
                    ) },
                    isError = showPortError,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    placeholder = {
                        Text(
                            text = defaultPort,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    readOnly = readOnly
                )
            }
        }
        if (!isDeviceAdded) {
            BasicConfirmButton(
                text = stringResource(R.string.update_parameters),
            ) {
                if (ipAddress.isBlank()) {
                    showIpAddressError = true
                }
                if (topic.isBlank()) {
                    showTopicError = true
                }
                if (userName.isBlank()) {
                    showUserNameError = true
                }
                if (password.isBlank()) {
                    showPasswordError = true
                }
                if (port.isBlank()) {
                    showPortError = true
                }
                if (!showIpAddressError && !showTopicError && !showUserNameError && !showPasswordError && !showPortError) {
                    viewModel.addDevice(ipAddress, topic, userName, password, port)
                    showToast = true
                }
            }
        } else {

            Box(
                contentAlignment = Alignment.BottomCenter
            ) {
                BasicConfirmButton(
                    text = stringResource(R.string.delete_device),
                    color = DarkRed
                ) {
                    isCheckBoxOpened = true
                }
                if (isCheckBoxOpened) {
                    CheckBox(
                        onClickNo = { isCheckBoxOpened = false },
                        onClickYes = {
                            viewModel.deleteDevice()
                            Toast.makeText(context, deviceDeletedMessage, Toast.LENGTH_SHORT)
                                .show()
                            isCheckBoxOpened = false
                            ipAddress = ""
                            topic = ""
                            userName = ""
                            password = ""
                            port = ""
                        }
                    )
                }
            }
        }
    }

}

@Composable
fun CheckBox(
    modifier: Modifier = Modifier,
    onClickYes: () -> Unit = {},
    onClickNo: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.if_delete_device),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.LightGray)
                    .clickable { onClickNo() }
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(DarkRed)
                    .clickable { onClickYes() }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.yes),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
