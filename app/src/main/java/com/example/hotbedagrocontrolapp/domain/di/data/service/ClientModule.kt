package com.example.hotbedagrocontrolapp.domain.di.data.service

import android.content.Context
import android.content.SharedPreferences
import com.example.hotbedagrocontrolapp.data.service.ClientImpl
import com.example.hotbedagrocontrolapp.domain.interfaces.data.service.Client
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClientModule {
    @Provides
    fun provideSharedPreferences(@ApplicationContext ctx: Context): SharedPreferences {
        return ctx.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    /**
     * Провайдер Mqtt клиента.
     */
    @Provides
    fun provideClient(prefs: SharedPreferences): Client {
        return ClientImpl(
            prefs.getString("ip_address", "") ?: "",
            prefs.getString("main_topic", "") ?: "",
            prefs.getString("user_name", "") ?: "",
            prefs.getString("password", "") ?: ""
        )
    }

}