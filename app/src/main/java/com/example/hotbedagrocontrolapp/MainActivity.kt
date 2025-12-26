package com.example.hotbedagrocontrolapp

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hotbedagrocontrolapp.data.db.DataBaseManager
import com.example.hotbedagrocontrolapp.presentation.ui.MainScreen
import com.example.hotbedagrocontrolapp.presentation.viewModel.elements.AgroControlViewModel
import com.example.hotbedagrocontrolapp.presentation.viewModel.statistics.StatisticsViewModel
import com.example.hotbedagrocontrolapp.service.ClientImpl
import com.example.hotbedagrocontrolapp.ui.theme.HotbedAgroControlAppTheme


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
    val statisticsViewModel = StatisticsViewModel(dataBaseManager)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            HotbedAgroControlAppTheme(dynamicColor = false, darkTheme = false) {
                MainScreen(
                    agroControlViewModel = agroControlViewModel,
                    statisticsViewModel = statisticsViewModel
                )
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
