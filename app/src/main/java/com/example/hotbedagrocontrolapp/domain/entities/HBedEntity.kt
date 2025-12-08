package com.example.hotbedagrocontrolapp.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hotbed_agro_control_history")
data class HBedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val time: String,
    val element: String,
    val response: String
)
