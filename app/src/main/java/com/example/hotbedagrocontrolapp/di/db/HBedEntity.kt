package com.example.hotbedagrocontrolapp.di.db

import androidx.room.Entity

/**
 * Строка таблицы с историей изменений элементов.
 */
@Entity(
    tableName = "hotbed_agro_control_history",
    primaryKeys = ["time", "element"]
)
data class HBedEntity(
    val time: String,
    val element: String,
    val response: String
)