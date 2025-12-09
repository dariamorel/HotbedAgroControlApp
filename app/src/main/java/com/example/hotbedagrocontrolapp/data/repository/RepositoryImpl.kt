package com.example.hotbedagrocontrolapp.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.hotbedagrocontrolapp.data.db.DataBaseManager
import com.example.hotbedagrocontrolapp.domain.entities.Control
import com.example.hotbedagrocontrolapp.domain.entities.ControlResponse
import com.example.hotbedagrocontrolapp.domain.entities.Element
import com.example.hotbedagrocontrolapp.domain.entities.Response
import com.example.hotbedagrocontrolapp.domain.interfaces.Client
import com.example.hotbedagrocontrolapp.domain.interfaces.Repository
import java.time.LocalDateTime

class RepositoryImpl(
    private val dataBaseManager: DataBaseManager,
    private val mqttClient: Client
): Repository {
    override suspend fun getData(
        element: Element,
        period: Pair<LocalDateTime, LocalDateTime>
    ): List<Response> {
        return dataBaseManager.getData(element, period)
    }

    override suspend fun insertData(element: Element, response: Response, time: LocalDateTime) {
        dataBaseManager.insertData(element, response, time)
    }

    override suspend fun publish(
        control: Control,
        status: ControlResponse.Status
    ) {
        mqttClient.publish(control.topic, status.message)
    }
}