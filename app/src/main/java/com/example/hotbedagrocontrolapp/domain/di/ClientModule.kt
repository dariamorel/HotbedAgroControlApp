package com.example.hotbedagrocontrolapp.domain.di

import com.example.hotbedagrocontrolapp.domain.interfaces.Client
import com.example.hotbedagrocontrolapp.service.ClientImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

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