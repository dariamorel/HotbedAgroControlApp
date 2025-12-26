package com.example.hotbedagrocontrolapp.domain.entities.elements

data class SensorResponse(override val data: Double): Response() {
    override val dataToString = data.toString()
    override val dataToDouble = data
}