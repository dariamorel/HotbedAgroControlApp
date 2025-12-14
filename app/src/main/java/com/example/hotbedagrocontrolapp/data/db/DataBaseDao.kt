package com.example.hotbedagrocontrolapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hotbedagrocontrolapp.domain.entities.Element
import com.example.hotbedagrocontrolapp.domain.entities.HBedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DataBaseDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertData(hBedEntity: HBedEntity)

    @Query("""
        SELECT time, response
        FROM hotbed_agro_control_history
        WHERE element = :element
        ORDER BY time ASC 
    """)
    fun getData(element: String): Flow<List<HistoryItem>>

    @Query("DELETE from hotbed_agro_control_history")
    fun cleanDataBase()
}

data class HistoryItem(
    val time: String,
    val response: String
)