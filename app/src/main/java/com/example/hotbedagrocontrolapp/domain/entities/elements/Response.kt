package com.example.hotbedagrocontrolapp.domain.entities.elements

sealed class Response {
    abstract val data: Any
    abstract val dataToString: String
    abstract val dataToDouble: Double
}