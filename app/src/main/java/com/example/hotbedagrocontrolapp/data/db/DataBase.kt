package com.example.hotbedagrocontrolapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hotbedagrocontrolapp.data.db.DataBaseDao
import com.example.hotbedagrocontrolapp.domain.entities.HBedEntity

@Database(
    entities = [HBedEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DataBase: RoomDatabase() {
    abstract val dataBaseDao: DataBaseDao
}