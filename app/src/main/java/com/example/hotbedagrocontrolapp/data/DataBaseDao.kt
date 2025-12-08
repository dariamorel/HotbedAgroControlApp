package com.example.hotbedagrocontrolapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hotbedagrocontrolapp.domain.entities.HBedEntity

@Dao
interface DataBaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(hBedEntity: HBedEntity)

    @Query("""
        SELECT response
        FROM hotbed_agro_control_history
        WHERE element = :element
        AND time BETWEEN :startTime AND :endTime
        ORDER BY time ASC 
    """)
    fun getData(element: String, startTime: String, endTime: String): List<String>

    @Query("DELETE from hotbed_agro_control_history")
    fun cleanDataBase()
}