package com.example.hotbedagrocontrolapp.data.service.aiService.entities

import com.google.gson.annotations.SerializedName

data class AiGenerateRequest(
    @SerializedName("model")
    val model: String,

    @SerializedName("prompt")
    val prompt: String,

    @SerializedName("stream")
    val stream: Boolean = false
)
