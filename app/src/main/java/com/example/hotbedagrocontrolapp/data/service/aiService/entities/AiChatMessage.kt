package com.example.hotbedagrocontrolapp.data.service.aiService.entities

import com.google.gson.annotations.SerializedName

data class AiChatMessage(
    @SerializedName("role")
    val role: String,

    @SerializedName("content")
    val content: String
)
