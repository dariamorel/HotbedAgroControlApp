package com.example.hotbedagrocontrolapp.data.db

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.hotbedagrocontrolapp.domain.entities.elements.Control
import com.example.hotbedagrocontrolapp.domain.entities.elements.ControlResponse
import com.example.hotbedagrocontrolapp.domain.entities.statistics.AnaliseType
import com.example.hotbedagrocontrolapp.domain.interfaces.entities.elements.Element
import com.example.hotbedagrocontrolapp.domain.entities.elements.Response
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.elements.SensorResponse
import com.example.hotbedagrocontrolapp.domain.entities.statistics.DateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

/**
 * Менеджер базы данных.
 */
class DataBaseManager @Inject constructor(
    private val dataBase: DataBase
) {

    /**
     * Вставить данные в таблицу.
     *
     * @param element Элемент.
     * @param response Значение элемента, полученное с устройства.
     * @param time Время, когда было получено значение.
     */
    suspend fun insertData(element: Element, response: Response, time: LocalDateTime) {
        dataBase.dataBaseDao.insertData(
            HBedEntity(
                time = time.toString(),
                element = element.name,
                response = response.dataToString
            )
        )
    }

    suspend fun insertData(entity: HBedEntity) {
        dataBase.dataBaseDao.insertData(entity)
    }


    /**
     * Поток истории по элементу за интервал, заданный типом анализа и опорной датой/временем.
     *
     * @param element Элемент.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getData(
        element: Element,
        dateTime: DateTime
    ): Flow<List<Pair<LocalDateTime, Response>>> {
        val (startTime, endTime) = when (dateTime.analiseType) {
            AnaliseType.HOUR -> {
                val start = dateTime.localDateTime.truncatedTo(ChronoUnit.HOURS)
                start to start.plusHours(1)
            }

            AnaliseType.DAY -> {
                val start = dateTime.localDateTime.truncatedTo(ChronoUnit.DAYS)
                start to start.plusDays(1)
            }

            AnaliseType.MONTH -> {
                val start = dateTime.localDateTime.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS)
                start to start.plusMonths(1)
            }

            AnaliseType.YEAR -> {
                val start = dateTime.localDateTime.withDayOfYear(1).truncatedTo(ChronoUnit.DAYS)
                start to start.plusYears(1)
            }
        }
        return dataBase.dataBaseDao.getData(
            element = element.name,
            startTime = startTime.toString(),
            endTime = endTime.toString()
        )
            .map { list ->
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

    /**
     * Очистить бд.
     */
    suspend fun clearDataBase() {
        dataBase.dataBaseDao.cleanDataBase()
    }

    companion object {
        const val DATA_BASE_TAG = "HBed DataBase"
    }
}