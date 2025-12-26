package com.example.hotbedagrocontrolapp.data.db

import androidx.room.Entity

@Entity(
    tableName = "hotbed_agro_control_history",
    primaryKeys = ["time", "element"]
)
data class HBedEntity(
    val time: String,
    val element: String,
    val response: String
)