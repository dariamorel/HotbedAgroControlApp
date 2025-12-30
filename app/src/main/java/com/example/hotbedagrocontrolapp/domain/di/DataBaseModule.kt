package com.example.hotbedagrocontrolapp.domain.di

import android.content.Context
import androidx.room.Room
import com.example.hotbedagrocontrolapp.data.db.DataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): DataBase {
        return Room.databaseBuilder(
            context,
            DataBase::class.java,
            "h_bed.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}