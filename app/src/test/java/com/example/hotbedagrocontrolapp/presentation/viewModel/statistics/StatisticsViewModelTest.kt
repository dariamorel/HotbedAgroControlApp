package com.example.hotbedagrocontrolapp.presentation.viewModel.statistics

import com.example.hotbedagrocontrolapp.MainDispatcherRule
import com.example.hotbedagrocontrolapp.data.db.DataBaseManager
import com.example.hotbedagrocontrolapp.data.service.dataService.DataServiceManager
import com.example.hotbedagrocontrolapp.domain.entities.elements.Control
import com.example.hotbedagrocontrolapp.domain.entities.elements.ControlResponse
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.elements.SensorResponse
import com.example.hotbedagrocontrolapp.domain.entities.statistics.AnaliseType
import com.example.hotbedagrocontrolapp.domain.entities.statistics.DateTime
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class StatisticsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val dataBaseManager = mockk<DataBaseManager>()
    private val dataServiceManager = mockk<DataServiceManager>(relaxed = true)
    private val viewModel = StatisticsViewModel(dataBaseManager, dataServiceManager)

    @Test
    fun `getDataHistory returns same cached flow for same key`() = runTest {
        val dateTime = DateTime(AnaliseType.HOUR, LocalDateTime.of(2026, 3, 22, 19, 57))
        every {
            dataBaseManager.getData(Sensor.AIR_TEMPERATURE, dateTime)
        } returns flowOf(
            listOf(
                LocalDateTime.of(2026, 3, 22, 19, 57) to SensorResponse(24.5)
            )
        )

        val first = viewModel.getDataHistory(Sensor.AIR_TEMPERATURE, dateTime)
        val second = viewModel.getDataHistory(Sensor.AIR_TEMPERATURE, dateTime)

        assertSame(first, second)
    }

    @Test
    fun `getDataHistory fills missing sensor points with zeros`() = runTest {
        val dateTime = DateTime(AnaliseType.DAY, LocalDateTime.of(2026, 3, 22, 19, 57))
        every {
            dataBaseManager.getData(Sensor.AIR_TEMPERATURE, dateTime)
        } returns flowOf(
            listOf(
                LocalDateTime.of(2026, 3, 22, 3, 15) to SensorResponse(20.0)
            )
        )

        val values = viewModel.getDataHistory(Sensor.AIR_TEMPERATURE, dateTime)
        val collected = values.first { it.isNotEmpty() }

        assertEquals(24, collected.size)
        assertEquals(20.0, collected[LocalDateTime.of(2026, 3, 22, 3, 0)]!!.dataToDouble, 0.0)
        assertEquals(0.0, collected[LocalDateTime.of(2026, 3, 22, 0, 0)]!!.dataToDouble, 0.0)
    }

    @Test
    fun `getDataHistory keeps control values as is`() = runTest {
        val dateTime = DateTime(AnaliseType.DAY, LocalDateTime.of(2026, 3, 22, 19, 57))
        every {
            dataBaseManager.getData(Control.RELAY_1, dateTime)
        } returns flowOf(
            listOf(
                LocalDateTime.of(2026, 3, 22, 10, 5) to ControlResponse(ControlResponse.Status.ON)
            )
        )

        val values = viewModel.getDataHistory(Control.RELAY_1, dateTime)
        val collected = values.first { it.isNotEmpty() }

        assertEquals(1, collected.size)
        assertEquals(
            1.0,
            collected[LocalDateTime.of(2026, 3, 22, 10, 5)]!!.dataToDouble,
            0.0
        )
    }
}
