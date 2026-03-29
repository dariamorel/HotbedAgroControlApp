package com.example.hotbedagrocontrolapp.di.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

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
            .addMigrations(DataBaseMigrations.MIGRATION_2_3)
            .build()
    }
}