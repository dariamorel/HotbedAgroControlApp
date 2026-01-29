package com.example.hotbedagrocontrolapp.domain.di.data.service

import com.example.hotbedagrocontrolapp.data.service.ClientImpl
import com.example.hotbedagrocontrolapp.domain.interfaces.data.service.Client
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ClientModule {

    @Provides
    fun provideClient(): Client {
        return ClientImpl(
            "80.237.33.119",
            "aha/HBed",
            "user_umki11",
            "654321"
        )
    }

}