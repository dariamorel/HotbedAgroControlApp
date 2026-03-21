package com.example.hotbedagrocontrolapp.data.service.dataService.entities

import com.google.gson.annotations.SerializedName

data class ElementResponse(
    @SerializedName("user_id")
    val userId: Long,

    @SerializedName("element")
    val element: String,

    @SerializedName("time")
    val time: String,

    @SerializedName("response")
    val response: String
)
