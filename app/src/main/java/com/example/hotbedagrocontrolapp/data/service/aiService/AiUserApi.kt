package com.example.hotbedagrocontrolapp.data.service.aiService

import com.example.hotbedagrocontrolapp.data.service.aiService.entities.AiGenerateRequest
import com.example.hotbedagrocontrolapp.data.service.aiService.entities.AiGenerateResponse
import com.example.hotbedagrocontrolapp.data.service.aiService.entities.AiChatRequest
import com.example.hotbedagrocontrolapp.data.service.aiService.entities.AiChatResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AiUserApi {
    @POST("api/generate")
    suspend fun generate(
        @Body request: AiGenerateRequest
    ): AiGenerateResponse

    @POST("api/chat")
    suspend fun chat(
        @Body request: AiChatRequest
    ): AiChatResponse
}