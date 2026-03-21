package com.example.hotbedagrocontrolapp.data.repository

import android.content.Context
import androidx.core.content.edit
import com.example.hotbedagrocontrolapp.data.service.dataService.UserApi
import com.example.hotbedagrocontrolapp.data.service.dataService.entities.UserCreate
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val userApi: UserApi,
    @ApplicationContext ctx: Context
) {
    private val prefs = ctx.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    suspend fun createUser(ipAddress: String, mainTopic: String, userName: String, password: String, port: Int) {
        val userResponse = userApi.createUser(UserCreate(
            ipAddress = ipAddress,
            topic = mainTopic,
            userName = userName,
            password = password,
            port = port
        ))
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
}