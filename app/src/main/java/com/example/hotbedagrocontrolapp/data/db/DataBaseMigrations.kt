package com.example.hotbedagrocontrolapp.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hotbedagrocontrolapp.domain.entities.elements.Control
import com.example.hotbedagrocontrolapp.domain.entities.elements.Sensor

object DataBaseMigrations {

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            val topicToEnumName: List<Pair<String, String>> =
                Sensor.entries.map { it.topic to it.name } +
                    Control.entries.map { it.topic to it.name }

            for ((topic, enumName) in topicToEnumName) {
                db.execSQL(
                    """
                    UPDATE hotbed_agro_control_history
                    SET element = ?
                    WHERE element = ?
                    """.trimIndent(),
                    arrayOf(enumName, topic)
                )
            }
        }
    }
}
