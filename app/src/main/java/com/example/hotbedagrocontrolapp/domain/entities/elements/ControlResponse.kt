package com.example.hotbedagrocontrolapp.domain.entities.elements

data class ControlResponse(override val data: Status): Response() {
    enum class Status(val message: String) {
        ON("ON"), OFF("OFF")
    }

    override val dataToString = data.message
    override val dataToDouble = if (data == Status.ON) 1.0 else 0.0
}