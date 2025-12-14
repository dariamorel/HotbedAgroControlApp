package com.example.hotbedagrocontrolapp.domain.entities

sealed class Response {
    abstract val data: Any
    abstract val dataToString: String
    abstract val dataToDouble: Double
}

data class SensorResponse(override val data: Double): Response() {
    override val dataToString = data.toString()
    override val dataToDouble = data
}

data class ControlResponse(override val data: Status): Response() {
    enum class Status(val message: String) {
        ON("ON"), OFF("OFF")
    }

    override val dataToString = data.message
    override val dataToDouble = if (data == Status.ON) 1.0 else 0.0
}