package com.example.hotbedagrocontrolapp.domain.viewModel.ai

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotbedagrocontrolapp.R
import com.example.hotbedagrocontrolapp.data.service.aiService.AiManager
import com.example.hotbedagrocontrolapp.data.service.aiService.entities.AiChatMessage
import com.example.hotbedagrocontrolapp.domain.entities.elements.Control
import com.example.hotbedagrocontrolapp.domain.entities.elements.Response
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.interfaces.entities.elements.Element
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiChatViewModel @Inject constructor(
    private val aiManager: AiManager,
    private val application: Application
) : ViewModel() {
    private val _chatHistory = MutableStateFlow<List<AiChatMessage>>(emptyList())
    val chatHistory = _chatHistory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _lastResponse = MutableStateFlow<String?>(null)
    val lastResponse = _lastResponse.asStateFlow()

    fun startChat(introMessage: String, currentData: Map<Element, Response>, optimalValues: Map<Sensor, Double?>) {
        val contextMessage = getContextMessage(currentData, optimalValues)
        _chatHistory.value = listOf(
            AiChatMessage(
                role = AiManager.ROLE_USER,
                content = contextMessage
            )
        )

        addMessage(introMessage)
    }

    fun clearChat() {
        _lastResponse.value = null
        _chatHistory.value = emptyList()
    }

    fun addMessage(
        message: String,
    ) {
        if (message.isBlank()) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            try {
                val updatedHistory = aiManager.sendUserMessage(
                    history = _chatHistory.value,
                    userMessage = message,
                )
                val assistantMessage = updatedHistory.lastOrNull {
                    it.role == AiManager.ROLE_ASSISTANT
                }?.content

                _chatHistory.value = updatedHistory
                _lastResponse.value = assistantMessage
            } catch (e: Exception) {
                Log.d(AI_TAG, "Error while getting answer: ${e.message}.")
                _lastResponse.value = e.message ?: "Unknown AI error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun getContextMessage(currentData: Map<Element, Response>, optimalValues: Map<Sensor, Double?>): String {
        val dataList = currentData.toList()
        val sensorsMessage = dataList.filter { (element, _) ->
            element is Sensor
        }.joinToString(", ") { (sensor, response) ->
            "${sensor.elementName}: ${response.dataToDouble} ${(sensor as Sensor).units}"
        }

        val controlsMessage = dataList.filter { (element, _) ->
            element is Control
        }.joinToString(", ") { (control, response) ->
            "${control.elementName}: ${response.dataToString}"
        }

        val optimalMessage = optimalValues.toList().filter { (_, value) ->
            value != null
        }.joinToString(", ") { (sensor, value) ->
            "${sensor.elementName}: $value ${(sensor).units}"
        }

        val contextMessage = application.getString(
            R.string.context_ai_chat_message,
            sensorsMessage,
            controlsMessage,
            optimalMessage
        )
        Log.d(AI_TAG, "Context message: $contextMessage")
        return contextMessage
    }

    companion object {
        const val AI_TAG = "AIChat"
    }
}
