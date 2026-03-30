package com.example.hotbedagrocontrolapp.data.service.aiService.entities

import com.google.gson.annotations.SerializedName

data class AiChatRequest(
    @SerializedName("model")
    val model: String,

    @SerializedName("messages")
    val messages: List<AiChatMessage>,

    @SerializedName("stream")
    val stream: Boolean = false
)
