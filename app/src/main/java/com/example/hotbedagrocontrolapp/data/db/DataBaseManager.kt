package com.example.hotbedagrocontrolapp.data.db

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.hotbedagrocontrolapp.domain.entities.Control
import com.example.hotbedagrocontrolapp.domain.entities.ControlResponse
import com.example.hotbedagrocontrolapp.domain.entities.Element
import com.example.hotbedagrocontrolapp.domain.entities.HBedEntity
import com.example.hotbedagrocontrolapp.domain.entities.Response
import com.example.hotbedagrocontrolapp.domain.entities.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.SensorResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime

class DataBaseManager(ctx: Context) {
    private val dataBase = buildDataBase(ctx)

    @RequiresApi(Build.VERSION_CODES.O)
    val dataHistory: Map<Element, Flow<List<Pair<LocalDateTime, Response>>>> =
        (Sensor.entries + Control.entries).associateWith { element ->
            getData(element)
        }

    suspend fun insertData(element: Element, response: Response, time: LocalDateTime) {
        dataBase.dataBaseDao.insertData(
            HBedEntity(
                time = time.toString(),
                element = element.topic,
                response = response.dataToString
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getData(
        element: Element
    ): Flow<List<Pair<LocalDateTime, Response>>> {
        return dataBase.dataBaseDao.getData(element.topic)
            .map{ list ->
                val result = list.mapNotNull { history ->
                    val time = history.time
                    val response = history.response

                    when (element.topic) {
                        in Sensor.entries.map { it.topic } -> {
                            try {
                                LocalDateTime.parse(time) to
                                        SensorResponse(response.toDouble())
                            } catch (e: Exception) {
                                Log.e(DATA_BASE_TAG, "Error parsing date: ${e.message}.")
                                null
                            }
                        }

                        in Control.entries.map { it.topic } -> {
                            try {
                                val dateTime = LocalDateTime.parse(time)
                                val controlResponse = when (response) {
                                    ControlResponse.Status.ON.message -> ControlResponse(ControlResponse.Status.ON)
                                    ControlResponse.Status.OFF.message -> ControlResponse(ControlResponse.Status.OFF)
                                    else -> {
                                        Log.e(
                                            DATA_BASE_TAG,
                                            "Response must be ON or OFF but is '$response' for element ${element.topic}."
                                        )
                                        return@mapNotNull null
                                    }
                                }
                                dateTime to controlResponse
                            } catch (e: Exception) {
                                Log.e(DATA_BASE_TAG, "Error parsing control data: ${e.message} (time=$time, response=$response)")
                                null
                            }
                        }

                        else -> {
                            Log.e(
                                DATA_BASE_TAG,
                                "Element must be from sensors or controls but is ${element.topic}."
                            )
                            null
                        }
                    }
                }
                result
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