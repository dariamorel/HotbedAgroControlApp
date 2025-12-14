package com.example.hotbedagrocontrolapp.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "hotbed_agro_control_history",
    primaryKeys = ["time", "element"]
)
data class HBedEntity(
    val time: String,
    val element: String,
    val response: String
)
