package com.example.hotbedagrocontrolapp.domain.entities.elements

/**
 * Значение, полученное с устройства об элементе.
 */
sealed class Response {
    /**
     * Значение элемента.
     */
    abstract val data: Any

    /**
     * Значение в строковом виде.
     */
    abstract val dataToString: String

    /**
     * Знаечние в вещественном виде.
     */
    abstract val dataToDouble: Double
}