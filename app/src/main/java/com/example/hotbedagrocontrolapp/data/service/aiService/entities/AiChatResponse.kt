package com.example.hotbedagrocontrolapp.data.service.aiService.entities

import com.google.gson.annotations.SerializedName

data class AiChatResponse(
    @SerializedName("model")
    val model: String,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("message")
    val message: AiChatMessage,

    @SerializedName("done")
    val done: Boolean,

    @SerializedName("done_reason")
    val doneReason: String? = null
)
