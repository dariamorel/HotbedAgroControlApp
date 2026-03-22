package com.example.hotbedagrocontrolapp.data.service.dataService

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import com.example.hotbedagrocontrolapp.data.db.DataBaseManager
import com.example.hotbedagrocontrolapp.data.db.HBedEntity
import com.example.hotbedagrocontrolapp.data.service.dataService.entities.ElementResponse
import com.example.hotbedagrocontrolapp.data.service.dataService.entities.UserCreate
import com.example.hotbedagrocontrolapp.domain.entities.statistics.AnaliseType
import com.example.hotbedagrocontrolapp.domain.interfaces.entities.elements.Element
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class DataServiceManager @Inject constructor(
    private val userApi: UserApi,
    private val dataBaseManager: DataBaseManager,
    @ApplicationContext ctx: Context
) {
    private val prefs = ctx.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val formatter = DateTimeFormatter.ISO_INSTANT
    private val toDbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")

    suspend fun createUser(ipAddress: String, mainTopic: String, userName: String, password: String, port: Int) {
        val userResponse = userApi.createUser(
            UserCreate(
                ipAddress = ipAddress,
                topic = mainTopic,
                userName = userName,
                password = password,
                port = port
            )
        )
        prefs.edit {
            putLong("user_id", userResponse.id)
        }
    }

    suspend fun deleteUser() {
        val userId = prefs.getLong("user_id", 0)
        userApi.deleteUser(userId)
        prefs.edit {
            remove("user_id")
        }
    }

    suspend fun getDataHistory(element: Element, time: LocalDateTime, period: AnaliseType) {
        val userId = prefs.getLong("user_id", 0)
        val timeStr = time.atZone(ZoneOffset.UTC).format(formatter)
        val elementListResponse =  userApi.getDataHistory(userId, element.name, timeStr, period.name)
        val elements = elementListResponse.content
        addNewDataToDb(elements)
        Log.d(DataServiceClient.DATA_SERVICE_TAG, "Elements from server: ${elementListResponse.totalElements}.")
    }

    private fun normalizeServerTimeToDb(isoOffsetDateTime: String): String =
        OffsetDateTime.parse(isoOffsetDateTime)
            .toLocalDateTime()
            .truncatedTo(ChronoUnit.MINUTES)
            .format(toDbFormatter)

    private suspend fun addNewDataToDb(elements: List<ElementResponse>) {
        for (response in elements) {
            val time = normalizeServerTimeToDb(response.time)
            dataBaseManager.insertData(
                HBedEntity(
                    time = time,
                    element = response.element,
                    response = response.response
                )
            )
        }
    }
}