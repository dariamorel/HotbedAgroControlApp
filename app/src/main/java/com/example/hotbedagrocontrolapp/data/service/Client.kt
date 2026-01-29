package com.example.hotbedagrocontrolapp.data.service

import android.util.Log
import com.example.hotbedagrocontrolapp.domain.interfaces.data.service.Client
import com.example.hotbedagrocontrolapp.domain.interfaces.data.service.Client.Companion.CLIENT_TAG
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

/**
 * Mqtt клиент.
 *
 * @param IPAddress IP адресс Mosquitto.
 * @param mainTopic Главный топик Mosquitto.
 * @param clientUserName Имя пользователя в Mosquitto.
 * @param clientPassword Пароль пользователя в Mosquitto.
 */
class ClientImpl(
    private val IPAddress: String,
    private val mainTopic: String,
    private val clientUserName: String,
    private val clientPassword: String,
): Client {
    private lateinit var mqttClient: MqttClient

    /**
     * Подключиться к Mosquitto.
     *
     * @param onMessageReceived Обработка получаемых сообщений с Mosquitto.
     * @param onConnectionLost Обработка потери связи.
     */
    override suspend fun connect(onMessageReceived: (String, String) -> Unit, onConnectionLost: () -> Unit) {
        val serverUri = "tcp://$IPAddress:12883"

        val options = MqttConnectOptions().apply {
            isAutomaticReconnect = true
            isCleanSession = false
            userName = clientUserName
            password = clientPassword.toCharArray()
        }

        mqttClient = MqttClient(
            serverUri,
            MqttClient.generateClientId(),
            MemoryPersistence()
        )

        mqttClient.connect(options)

        mqttClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) { onConnectionLost() }

            override fun messageArrived(
                topic: String?,
                message: MqttMessage?
            ) {
                val topicString = topic.toString()
                val messageString = message.toString()
                Log.e(CLIENT_TAG, "Message received: $topicString, $messageString.")
                if (topicString.isEmpty()) {
                    Log.e(CLIENT_TAG, "Received topic is null or empty.")
                    return
                }
                if (messageString.isEmpty()) {
                    Log.e(CLIENT_TAG, "Received message is null or empty.")
                    return
                }
                onMessageReceived(topicString, messageString)
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {}
        })

        mqttClient.subscribe("$mainTopic/#", 1)
    }

    /**
     * Отправить сообщение в Mosquitto.
     *
     * @param topic Топик Mosquitto.
     * @param message Передаваемое сообщение.
     */
    override suspend fun publish(topic: String, message: String) {
        mqttClient.publish("$mainTopic/$topic/cmd_t", MqttMessage(message.toByteArray()))
        mqttClient.publish("$mainTopic/$topic/stat_t", MqttMessage(message.toByteArray()))
    }

    /**
     * Отсоединиться.
     */
    override suspend fun disconnect() = mqttClient.disconnect()
}