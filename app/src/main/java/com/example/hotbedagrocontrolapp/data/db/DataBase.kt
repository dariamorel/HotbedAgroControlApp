package com.example.hotbedagrocontrolapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hotbedagrocontrolapp.data.db.DataBaseDao
import com.example.hotbedagrocontrolapp.data.db.HBedEntity

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