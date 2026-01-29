package com.example.hotbedagrocontrolapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hotbedagrocontrolapp.data.db.HBedEntity
import kotlinx.coroutines.flow.Flow

/**
 * Dao к базе данных.
 */
@Dao
interface DataBaseDao {
    /**
     *  Вставить строку в таблицу.
     *
     *  @param hBedEntity Entity таблицы.
     */
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertData(hBedEntity: HBedEntity)

    /**
     * Получить данные по элементу.
     *
     * @param element Элемент (датчик или сенсор).
     * @return Список с изменениями по данному элементу.
     */
    @Query("""
        SELECT time, response
        FROM hotbed_agro_control_history
        WHERE element = :element
        ORDER BY time ASC 
    """)
    fun getData(element: String): Flow<List<HistoryItem>>

    /**
     * Очистить базу данных.
     */
    @Query("DELETE from hotbed_agro_control_history")
    fun cleanDataBase()
}

data class HistoryItem(
    val time: String,
    val response: String
)