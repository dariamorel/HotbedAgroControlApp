package com.example.hotbedagrocontrolapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hotbedagrocontrolapp.domain.entities.HBedEntity

@Database(
    entities = [HBedEntity::class],
    version = 1
)
abstract class DataBase: RoomDatabase() {
    abstract val dataBaseDao: DataBaseDao
}

