package com.example.hotbedagrocontrolapp.domain.di.data.db

import android.content.Context
import androidx.room.Room
import com.example.hotbedagrocontrolapp.data.db.DataBase
import com.example.hotbedagrocontrolapp.data.db.DataBaseMigrations
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