package com.example.hotbedagrocontrolapp.data.service.dataService

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.hotbedagrocontrolapp.di.db.DataBaseManager
import com.example.hotbedagrocontrolapp.di.db.HBedEntity
import com.example.hotbedagrocontrolapp.data.service.dataService.entities.ElementListResponse
import com.example.hotbedagrocontrolapp.data.service.dataService.entities.ElementResponse
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor
import com.example.hotbedagrocontrolapp.domain.entities.statistics.AnaliseType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class DataServiceManagerTest {

    private val userApi = mockk<UserApi>()
    private val dataBaseManager = mockk<DataBaseManager>()
    private val context = mockk<Context>()
    private val prefs = mockk<SharedPreferences>()

    private lateinit var manager: DataServiceManager

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE) } returns prefs
        every { prefs.getLong("user_id", 0) } returns 42L
        manager = DataServiceManager(userApi, dataBaseManager, context)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getDataHistory sends correct request params`() = runBlocking {
        val time = LocalDateTime.of(2026, 3, 22, 19, 57)
        val expectedTime = time.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)

        coEvery {
            userApi.getDataHistory(42L, Sensor.AIR_TEMPERATURE.name, expectedTime, AnaliseType.DAY.name)
        } returns ElementListResponse(emptyList(), 0)
        coEvery { dataBaseManager.insertData(any<HBedEntity>()) } just runs

        manager.getDataHistory(Sensor.AIR_TEMPERATURE, time, AnaliseType.DAY)

        coVerify(exactly = 1) {
            userApi.getDataHistory(42L, Sensor.AIR_TEMPERATURE.name, expectedTime, AnaliseType.DAY.name)
        }
    }

    @Test
    fun `getDataHistory saves normalized time to db`() = runBlocking {
        val serverResponse = ElementResponse(
            userId = 42L,
            element = Sensor.AIR_TEMPERATURE.name,
            time = "2026-03-22T19:57:09.742387+03:00",
            response = "24.5"
        )

        coEvery {
            userApi.getDataHistory(any(), any(), any(), any())
        } returns ElementListResponse(listOf(serverResponse), 1)
        coEvery { dataBaseManager.insertData(any<HBedEntity>()) } just runs

        manager.getDataHistory(
            Sensor.AIR_TEMPERATURE,
            LocalDateTime.of(2026, 3, 22, 19, 57),
            AnaliseType.HOUR
        )

        coVerify(exactly = 1) {
            dataBaseManager.insertData(
                match<HBedEntity> {
                    it.time == "2026-03-22T19:57" &&
                        it.element == Sensor.AIR_TEMPERATURE.name &&
                        it.response == "24.5"
                }
            )
        }
    }
}
