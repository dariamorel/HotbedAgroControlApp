package com.example.hotbedagrocontrolapp.data.service.aiService

import com.example.hotbedagrocontrolapp.data.service.aiService.entities.AiGenerateRequest
import com.example.hotbedagrocontrolapp.data.service.aiService.entities.AiGenerateResponse
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

    suspend fun generateText(
        prompt: String,
        model: String = DEFAULT_MODEL
    ): String {
        return generate(prompt = prompt, model = model, stream = false).response.trim()
    }

    companion object {
        const val DEFAULT_MODEL = "qwen3:1.7b"
    }
}
