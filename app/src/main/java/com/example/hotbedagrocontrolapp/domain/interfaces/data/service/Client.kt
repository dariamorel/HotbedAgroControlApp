package com.example.hotbedagrocontrolapp.domain.interfaces.data.service

/**
 * Mqtt клиент.
 */
interface Client {

    /**
     * Подключиться к Mosquitto.
     *
     * @param onMessageReceived Обработка получаемых сообщений с Mosquitto.
     */
    suspend fun connect(onMessageReceived: (String, String) -> Unit)

    /**
     * Отправить сообщение в Mosquitto.
     *
     * @param topic Топик Mosquitto.
     * @param message Передаваемое сообщение.
     */
    suspend fun publish(topic: String, message: String)

    /**
     * Отсоединиться.
     */
    suspend fun disconnect()

    companion object {
        const val CLIENT_TAG = "Mqtt client"
    }
}