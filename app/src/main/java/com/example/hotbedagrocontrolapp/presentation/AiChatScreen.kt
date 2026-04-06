package com.example.hotbedagrocontrolapp.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hotbedagrocontrolapp.R
import com.example.hotbedagrocontrolapp.data.service.aiService.AiManager
import com.example.hotbedagrocontrolapp.data.service.aiService.entities.AiChatMessage
import com.example.hotbedagrocontrolapp.domain.viewModel.ai.AiChatViewModel
import com.example.hotbedagrocontrolapp.domain.viewModel.elements.AgroControlViewModel
import com.example.hotbedagrocontrolapp.presentation.components.ai.ChatFrame
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicBackArrow
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicOpenButton
import com.example.hotbedagrocontrolapp.presentation.components.basicComponents.BasicTextField
import com.example.hotbedagrocontrolapp.ui.theme.DarkBlue
import com.example.hotbedagrocontrolapp.ui.theme.DarkOrange
import com.example.hotbedagrocontrolapp.ui.theme.DarkRed

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AiChatScreen(
    agroControlViewModel: AgroControlViewModel,
    aiChatViewModel: AiChatViewModel,
    modifier: Modifier = Modifier,
    introMessage: String? = null,
    onBack: () -> Unit = {}
) {
    val currentData by agroControlViewModel.currentData.collectAsState()
    val optimalValues by agroControlViewModel.optimalValues.collectAsState()

    val chatHistory by aiChatViewModel.chatHistory.collectAsState()
    val isLoading by aiChatViewModel.isLoading.collectAsState()
    val chatStarted by aiChatViewModel.charStarted.collectAsState()
    val listState = rememberLazyListState()
    val visibleMessages = if (chatHistory.size > 1) chatHistory.drop(1) else emptyList()

    var message by remember { mutableStateOf(introMessage ?: "") }

    LaunchedEffect(chatStarted) {
        if (!chatStarted) {
            aiChatViewModel.startChat(
                currentData = currentData,
                optimalValues = optimalValues
            )
        }
    }

    LaunchedEffect(visibleMessages.size, isLoading) {
        if (visibleMessages.isNotEmpty()) {
            listState.animateScrollToItem(visibleMessages.lastIndex)
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        CloseButton(onBack = { onBack() }) {
            aiChatViewModel.clearChat()
            message = ""
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LazyColumn(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .weight(1f),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(visibleMessages) { item ->
                    val isUser = item.role == "user"
                    ChatFrame(item.content, isUser)
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(message, Modifier.weight(1f)) { newMessage ->
                    message = newMessage
                }
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            if (message.isNotBlank()) {
                                aiChatViewModel.addMessage(message)
                                message = ""
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send message",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CloseButton(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(MaterialTheme.colorScheme.surface),
    ) {
        BasicBackArrow(Modifier.padding(horizontal = 4.dp)) { onBack() }
        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .padding(vertical = 4.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.ai_chat_clear),
                style = MaterialTheme.typography.titleSmall.copy(
                    textDecoration = TextDecoration.Underline
                ),
                textAlign = TextAlign.End,
                color = DarkBlue
            )
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear chat",
                tint = DarkRed,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}