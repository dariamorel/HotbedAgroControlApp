package com.example.hotbedagrocontrolapp.data.service.dataService.entities

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("ip_address")
    val ipAddress: String,

    @SerializedName("topic")
    val topic: String,

    @SerializedName("user_name")
    val userName: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("port")
    val port: Int
)
