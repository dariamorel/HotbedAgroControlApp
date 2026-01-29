package com.example.hotbedagrocontrolapp.domain.entities.elements

/**
 * Сообщение со значением датчика, получаемое с устройства.
 *
 * @param data Значение датчика.
 */
data class SensorResponse(override val data: Double): Response() {
    override val dataToString = data.toString()
    override val dataToDouble = data
}