package com.example.hotbedagrocontrolapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.hotbedagrocontrolapp.domain.interfaces.Client.Companion.CLIENT_TAG
import com.example.hotbedagrocontrolapp.service.ClientImpl
import com.example.hotbedagrocontrolapp.ui.theme.HotbedAgroControlAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var messageState by mutableStateOf("")
    private var topicState by mutableStateOf("")

    private val scope = CoroutineScope(Dispatchers.IO)

    val client = ClientImpl(
        "80.237.33.119",
        "aha/HBed",
        "user_umki11",
        "654321"
    ) { topic, message ->
        messageState = message
        topicState = topic
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scope.launch {
            try {
                client.connect()
                client.publish("aha/HBed/ClearCloudy/cmd_t", "OFF")
                Log.d(CLIENT_TAG, "Connected!")
            } catch(e: Exception) {
                Log.e(CLIENT_TAG, "Connection error: ${e.message}")
            }
        }

        enableEdgeToEdge()
        setContent {
            HotbedAgroControlAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        topic = topicState,
                        message = messageState,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.launch {
            try {
                client.disconnect()
            } catch(e: Exception) {
                Log.e(CLIENT_TAG, "Disconnection error: ${e.message}")
            }
        }
    }
}

@Composable
fun Greeting(topic: String, message: String, modifier: Modifier = Modifier) {
    Text(
        text = "Topic: $topic, message: $message",
        modifier = modifier
    )
}
