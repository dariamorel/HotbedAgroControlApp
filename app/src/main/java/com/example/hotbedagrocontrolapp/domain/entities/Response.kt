package com.example.hotbedagrocontrolapp.domain.entities

sealed class Response {
    abstract val data: Any
}

data class SensorResponse(override val data: Double): Response()

data class ControlResponse(override val data: Status): Response() {
    enum class Status(val message: String) {
        ON("ON"), OFF("OFF")
    }
}