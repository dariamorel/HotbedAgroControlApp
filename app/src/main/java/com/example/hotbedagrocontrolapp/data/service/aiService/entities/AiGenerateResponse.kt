package com.example.hotbedagrocontrolapp.data.service.aiService.entities

import com.google.gson.annotations.SerializedName

data class AiGenerateResponse(
    @SerializedName("model")
    val model: String,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("response")
    val response: String,

    @SerializedName("done")
    val done: Boolean,

    @SerializedName("done_reason")
    val doneReason: String? = null
)
