package com.example.hotbedagrocontrolapp.service

import android.util.Log
import com.example.hotbedagrocontrolapp.domain.interfaces.Client
import com.example.hotbedagrocontrolapp.domain.interfaces.Client.Companion.CLIENT_TAG
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class ClientImpl(
    private val IPAddress: String,
    private val mainTopic: String,
    private val clientUserName: String,
    private val clientPassword: String,
    private val onMessageReceived: (String, String) -> Unit
): Client {
    private lateinit var mqttClient: MqttClient

    override suspend fun connect() {
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
            override fun connectionLost(cause: Throwable?) {}

            override fun messageArrived(
                topic: String?,
                message: MqttMessage?
            ) {
                Log.e(CLIENT_TAG, "Message was received.")
                val topicString = topic.toString()
                val messageString = message.toString()
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

    override suspend fun publish(topic: String, message: String) {
        mqttClient.publish("$mainTopic/$topic/cmd_t", MqttMessage(message.toByteArray()))
        mqttClient.publish("$mainTopic/$topic/stat_t", MqttMessage(message.toByteArray()))
    }

    override suspend fun disconnect() = mqttClient.disconnect()
}