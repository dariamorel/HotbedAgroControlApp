package com.example.hotbedagrocontrolapp.di.db

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * База данных с историей изменения показателей устройства.
 */
@Database(
    entities = [HBedEntity::class],
    version = 3,
    exportSchema = false
)
abstract class DataBase: RoomDatabase() {
    abstract val dataBaseDao: DataBaseDao
}