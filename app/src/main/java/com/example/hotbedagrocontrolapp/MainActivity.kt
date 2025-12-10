package com.example.hotbedagrocontrolapp

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.hotbedagrocontrolapp.data.db.DataBaseManager
import com.example.hotbedagrocontrolapp.domain.interfaces.Client.Companion.CLIENT_TAG
import com.example.hotbedagrocontrolapp.presentation.ui.ElementsScreen
import com.example.hotbedagrocontrolapp.presentation.viewModel.AgroControlViewModel
import com.example.hotbedagrocontrolapp.service.ClientImpl
import com.example.hotbedagrocontrolapp.ui.theme.HotbedAgroControlAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HBedApp: Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}

class MainActivity : ComponentActivity() {

    val mqttClient = ClientImpl(
        "80.237.33.119",
        "aha/HBed",
        "user_umki11",
        "654321"
    )
    val dataBaseManager = DataBaseManager(HBedApp.appContext)
    @RequiresApi(Build.VERSION_CODES.O)
    val agroControlViewModel = AgroControlViewModel(dataBaseManager, mqttClient)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            HotbedAgroControlAppTheme(dynamicColor = false) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    ElementsScreen(
                        viewModel = agroControlViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        super.onDestroy()
        agroControlViewModel.disconnect()
    }
}

@Composable
fun Greeting(topic: String, message: String, modifier: Modifier = Modifier) {
    Text(
        text = "Topic: $topic, message: $message",
        modifier = modifier
    )
}
