package com.example.hotbedagrocontrolapp.domain.entities.devices

/**
 * Параметры Mqtt.
 *
 * @param ipAddress IP адрес Mosquitto.
 * @param mainTopic Главный топик Mosquitto.
 * @param userName Имя пользователя в Mosquitto.
 * @param password Пароль пользователя в Mosquitto.
 */
data class MqttSettings(
    val ipAddress: String,
    val mainTopic: String,
    val userName: String,
    val password: String,
    val port: String
)
