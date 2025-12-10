package com.example.hotbedagrocontrolapp.domain.interfaces

interface Client {
    suspend fun connect(onMessageReceived: (String, String) -> Unit)
    suspend fun publish(topic: String, message: String)
    suspend fun disconnect()

    companion object {
        const val CLIENT_TAG = "Mqtt client"
    }
}