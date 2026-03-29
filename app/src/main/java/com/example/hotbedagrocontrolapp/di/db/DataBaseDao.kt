package com.example.hotbedagrocontrolapp.di.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
     * Получить данные по элементу за интервал времени.
     *
     * @param element Топик элемента.
     * @param startTime Начало интервала.
     * @param endTime Конец интервала включительно.
     */
    @Query(
        """
        SELECT time, response
        FROM hotbed_agro_control_history
        WHERE element = :element
            AND time >= :startTime
            AND time <= :endTime
        ORDER BY time ASC
        """
    )
    fun getData(
        element: String,
        startTime: String,
        endTime: String
    ): Flow<List<HistoryItem>>

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