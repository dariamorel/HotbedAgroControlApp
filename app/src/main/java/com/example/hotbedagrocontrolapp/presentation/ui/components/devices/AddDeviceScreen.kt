package com.example.hotbedagrocontrolapp.presentation.ui.components.devices

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotbedagrocontrolapp.presentation.viewModel.elements.AgroControlViewModel

/***
 * Экран для добавления нового устройства.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddDeviceScreen(
    viewModel: AgroControlViewModel,
    modifier: Modifier = Modifier
) {
    var ipAddress by remember { mutableStateOf("") }
    var mainTopic by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Column(modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TextField(
            value = ipAddress,
            onValueChange = { newIpAddress -> ipAddress = newIpAddress },
            label = { Text("IP адрес", color = MaterialTheme.colorScheme.onPrimary) },
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp)),
            colors = TextFieldDefaults.colors(focusedContainerColor = Color.LightGray, unfocusedContainerColor = MaterialTheme.colorScheme.surface)
        )
        TextField(
            value = mainTopic,
            onValueChange = { newMainTopic -> mainTopic = newMainTopic },
            label = { Text("Топик", color = MaterialTheme.colorScheme.onPrimary) },
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp)),
            colors = TextFieldDefaults.colors(focusedContainerColor = Color.LightGray, unfocusedContainerColor = MaterialTheme.colorScheme.surface)
        )
        TextField(
            value = userName,
            onValueChange = { newUserName -> userName = newUserName },
            label = { Text("Имя пользователя", color = MaterialTheme.colorScheme.onPrimary) },
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp)),
            colors = TextFieldDefaults.colors(focusedContainerColor = Color.LightGray, unfocusedContainerColor = MaterialTheme.colorScheme.surface)
        )
        TextField(
            value = password,
            onValueChange = { newPassword -> password = newPassword },
            label = { Text("Пароль", color = MaterialTheme.colorScheme.onPrimary) },
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp)),
            colors = TextFieldDefaults.colors(focusedContainerColor = Color.LightGray, unfocusedContainerColor = MaterialTheme.colorScheme.surface)
        )
        Button(
            onClick = { viewModel.addDevice(ipAddress, mainTopic, userName, password) },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
        ) {
            Text(
                text = "Добавить устройство",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}