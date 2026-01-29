package com.example.hotbedagrocontrolapp.domain.di.data.db

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

    /**
     * Провайдер базы данных.
     */
    @Provides
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