package com.example.hotbedagrocontrolapp.data.db

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.hotbedagrocontrolapp.domain.entities.Control
import com.example.hotbedagrocontrolapp.domain.entities.ControlResponse
import com.example.hotbedagrocontrolapp.domain.entities.Element
import com.example.hotbedagrocontrolapp.domain.entities.HBedEntity
import com.example.hotbedagrocontrolapp.domain.entities.Response
import com.example.hotbedagrocontrolapp.domain.entities.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.SensorResponse
import java.time.LocalDateTime

class DataBaseManager(ctx: Context) {
    private val dataBase = buildDataBase(ctx)

    suspend fun insertData(element: Element, response: Response, time: LocalDateTime) {
        dataBase.dataBaseDao.insertData(
            HBedEntity(
                time = time.toString(),
                element = element.toString(),
                response = response.toString()
            )
        )
    }

    suspend fun getData(
        element: Element,
        period: Pair<LocalDateTime, LocalDateTime>
    ): List<Response> {
        return dataBase.dataBaseDao.getData(
            element.topic,
            period.first.toString(),
            period.second.toString()
        ).mapNotNull { response ->
            when (element.topic) {
                in Sensor.entries.map { it.topic } -> SensorResponse(response.toDouble())
                in Control.entries.map { it.topic } -> {
                    when (response) {
                        "ON" -> ControlResponse(ControlResponse.Status.ON)
                        "OFF" -> ControlResponse(ControlResponse.Status.OFF)
                        else -> {
                            Log.e(DATA_BASE_TAG, "Response must be ON or OFF but is $response.")
                            null
                        }
                    }
                }
                else -> {
                    Log.e(DATA_BASE_TAG, "Element must be from sensors or controls but is ${element.topic}.")
                    null
                }
            }
        }
    }

    suspend fun clearDataBase() {
        dataBase.dataBaseDao.cleanDataBase()
    }

    private fun buildDataBase(ctx: Context): DataBase {
        return Room
            .databaseBuilder(
                ctx,
                DataBase::class.java,
                "h_bed.db"
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    companion object {
        const val DATA_BASE_TAG = "HBed DataBase"
    }
}