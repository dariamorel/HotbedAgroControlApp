package com.example.hotbedagrocontrolapp.data.service.aiService

import com.example.hotbedagrocontrolapp.data.service.aiService.entities.AiGenerateRequest
import com.example.hotbedagrocontrolapp.data.service.aiService.entities.AiGenerateResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AiUserApi {
    @POST("api/generate")
    suspend fun generate(
        @Body request: AiGenerateRequest
    ): AiGenerateResponse
}