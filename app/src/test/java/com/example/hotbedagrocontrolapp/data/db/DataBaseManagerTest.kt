package com.example.hotbedagrocontrolapp.data.db

import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.elements.SensorResponse
import com.example.hotbedagrocontrolapp.domain.entities.statistics.AnaliseType
import com.example.hotbedagrocontrolapp.domain.entities.statistics.DateTime
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class DataBaseManagerTest {

    private val dataBase = mockk<DataBase>()
    private val dao = mockk<DataBaseDao>()
    private val manager = DataBaseManager(dataBase)

    init {
        every { dataBase.dataBaseDao } returns dao
    }

    @Test
    fun `getData maps sensor history from db`() = runBlocking {
        every {
            dao.getData(
                Sensor.AIR_TEMPERATURE.name,
                "2026-03-22T19:00",
                "2026-03-22T20:00"
            )
        } returns flowOf(
            listOf(
                HistoryItem("2026-03-22T19:57", "24.5")
            )
        )

        val values = manager.getData(
            Sensor.AIR_TEMPERATURE,
            DateTime(AnaliseType.HOUR, LocalDateTime.of(2026, 3, 22, 19, 57))
        ).first()

        assertEquals(1, values.size)
        assertEquals(LocalDateTime.of(2026, 3, 22, 19, 57), values.first().first)
        assertEquals(24.5, values.first().second.dataToDouble, 0.0)
    }
}
