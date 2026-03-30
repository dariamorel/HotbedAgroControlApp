package com.example.hotbedagrocontrolapp.data.service.aiService

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AiClient {
    private const val BASE_URL = "http://smarttherm.ru:11434/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttp)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val aiUserApi: AiUserApi = retrofit.create(AiUserApi::class.java)

    const val AI_SERVICE_TAG = "AiService"
}
