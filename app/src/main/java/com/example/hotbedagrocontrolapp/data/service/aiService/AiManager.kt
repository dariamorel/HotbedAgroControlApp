package com.example.hotbedagrocontrolapp.data.service.aiService

import android.util.Log
import com.example.hotbedagrocontrolapp.data.service.aiService.entities.AiChatMessage
import com.example.hotbedagrocontrolapp.data.service.aiService.entities.AiChatRequest
import com.example.hotbedagrocontrolapp.data.service.aiService.entities.AiChatResponse
import com.example.hotbedagrocontrolapp.data.service.aiService.entities.AiGenerateRequest
import com.example.hotbedagrocontrolapp.data.service.aiService.entities.AiGenerateResponse
import com.example.hotbedagrocontrolapp.domain.viewModel.ai.AiChatViewModel
import com.example.hotbedagrocontrolapp.presentation.AiChatScreen
import javax.inject.Inject

class AiManager @Inject constructor(
    private val aiUserApi: AiUserApi
) {
    suspend fun generate(
        prompt: String,
        model: String = DEFAULT_MODEL,
        stream: Boolean = false
    ): AiGenerateResponse {
        return aiUserApi.generate(
            AiGenerateRequest(
                model = model,
                prompt = prompt,
                stream = stream
            )
        )
    }


    suspend fun sendUserMessage(
        history: List<AiChatMessage>,
        userMessage: String,
    ): List<AiChatMessage> {
        val updatedHistory = history + AiChatMessage(
            role = ROLE_USER,
            content = userMessage
        )
        Log.d(AiChatViewModel.AI_TAG, "Content: $updatedHistory")
        val response = aiUserApi.chat(
            AiChatRequest(
                model = DEFAULT_MODEL,
                messages = updatedHistory,
                stream = false
            )
        )
        return updatedHistory + response.message
    }

    companion object {
        const val DEFAULT_MODEL = "gemma2:latest"
        const val ROLE_USER = "user"
    }
}
