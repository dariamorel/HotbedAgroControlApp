package com.example.hotbedagrocontrolapp.data.service.aiService

import android.util.Log
import com.example.hotbedagrocontrolapp.data.service.aiService.entities.AiChatMessage
import com.example.hotbedagrocontrolapp.data.service.aiService.entities.AiChatRequest
import com.example.hotbedagrocontrolapp.data.service.aiService.entities.AiGenerateRequest
import com.example.hotbedagrocontrolapp.data.service.aiService.entities.AiGenerateResponse
import com.example.hotbedagrocontrolapp.domain.viewModel.ai.AiChatViewModel
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
    ): List<AiChatMessage> {
        Log.d(
            AiChatViewModel.AI_TAG,
            "Sending chat request. model=$DEFAULT_MODEL, messages=${history.size}"
        )
        val response = aiUserApi.chat(
            AiChatRequest(
                model = DEFAULT_MODEL,
                messages = history,
                stream = false
            )
        )
        Log.d(
            AiChatViewModel.AI_TAG,
            "Chat response received. done=${response.done}, reason=${response.doneReason}, role=${response.message.role}"
        )
        return history + response.message
    }

    companion object {
        const val DEFAULT_MODEL = "qwen3:1.7b"
        const val ROLE_USER = "user"
        const val ROLE_ASSISTANT = "assistant"
    }
}
