package com.example.hotbedagrocontrolapp.data.service.dataService

import com.example.hotbedagrocontrolapp.data.service.dataService.entities.ElementListResponse
import com.example.hotbedagrocontrolapp.data.service.dataService.entities.UserCreate
import com.example.hotbedagrocontrolapp.data.service.dataService.entities.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @POST("users")
    suspend fun createUser(
        @Body user: UserCreate
    ): UserResponse

    @DELETE("users/{id}")
    suspend fun deleteUser(
        @Path("id") id: Long
    )

    @GET("history/{id}")
    suspend fun getDataHistory(
        @Path("id") id: Long,
        @Query("element") element: String,
        @Query("time") time: String,
        @Query("period") period: String
    ): ElementListResponse
}