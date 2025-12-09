package com.example.hotbedagrocontrolapp.domain.interfaces

import com.example.hotbedagrocontrolapp.domain.entities.Control
import com.example.hotbedagrocontrolapp.domain.entities.ControlResponse
import com.example.hotbedagrocontrolapp.domain.entities.Element
import com.example.hotbedagrocontrolapp.domain.entities.Response
import java.time.LocalDateTime

interface Repository {
    suspend fun getData(element: Element, period: Pair<LocalDateTime, LocalDateTime>): List<Response>
    suspend fun insertData(element: Element, response: Response, time: LocalDateTime)
    suspend fun publish(control: Control, status: ControlResponse.Status)
}