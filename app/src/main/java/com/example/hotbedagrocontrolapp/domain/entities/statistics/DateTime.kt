package com.example.hotbedagrocontrolapp.domain.entities.statistics

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class DateTime(
    val analiseType: AnaliseType,
    dateTime: LocalDateTime
) {
    val localDateTime: LocalDateTime = when (analiseType) {
        AnaliseType.YEAR -> LocalDateTime.of(
            dateTime.year,
            1,
            1,
            0,
            0)
        AnaliseType.MONTH -> LocalDateTime.of(
            dateTime.year,
            dateTime.month,
            1,
            0,
            0)
        AnaliseType.DAY -> LocalDateTime.of(
            dateTime.year,
            dateTime.month,
            dateTime.dayOfMonth,
            0,
            0
        )
        AnaliseType.HOUR -> LocalDateTime.of(
            dateTime.year,
            dateTime.month,
            dateTime.dayOfMonth,
            dateTime.hour,
            0
        )
    }

    constructor(analiseType: AnaliseType) : this(analiseType, LocalDateTime.now())

    fun plus(amount: Long): DateTime {
        return when (analiseType) {
            AnaliseType.HOUR -> DateTime(analiseType, localDateTime.plusHours(amount))
            AnaliseType.DAY -> DateTime(analiseType, localDateTime.plusDays(amount))
            AnaliseType.MONTH -> DateTime(analiseType, localDateTime.plusMonths(amount))
            AnaliseType.YEAR -> DateTime(analiseType, localDateTime.plusYears(amount))
        }
    }

    fun minus(amount: Long): DateTime {
        return when (analiseType) {
            AnaliseType.HOUR -> DateTime(analiseType, localDateTime.minusHours(amount))
            AnaliseType.DAY -> DateTime(analiseType, localDateTime.minusDays(amount))
            AnaliseType.MONTH -> DateTime(analiseType, localDateTime.minusMonths(amount))
            AnaliseType.YEAR -> DateTime(analiseType, localDateTime.minusYears(amount))
        }
    }

    val iterator: Iterator
        get() = Iterator()

    val fullString: String
        get() = when (analiseType) {
            AnaliseType.HOUR -> localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HHч"))
            AnaliseType.DAY -> localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            AnaliseType.MONTH -> localDateTime.format(DateTimeFormatter.ofPattern("LLLL yyyy", Locale("ru")))
            AnaliseType.YEAR -> localDateTime.format(DateTimeFormatter.ofPattern("yyyyг"))
        }

    inner class Iterator {
        var localDateTime = this@DateTime.localDateTime

        fun plus(i: Long): String {
            return when (this@DateTime.analiseType) {
                AnaliseType.HOUR -> {
                    val newLocalDateTime = localDateTime.plusMinutes(i)
                    newLocalDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                }
                AnaliseType.DAY -> {
                    val newLocalDateTime = localDateTime.plusHours(i)
                    newLocalDateTime.format(DateTimeFormatter.ofPattern("HHч"))
                }
                AnaliseType.MONTH -> {
                    val newLocalDateTime = localDateTime.plusDays(i)
                    newLocalDateTime.format(DateTimeFormatter.ofPattern("dd.MM"))
                }
                AnaliseType.YEAR -> {
                    val newLocalDateTime = localDateTime.plusMonths(i)
                    newLocalDateTime.format(DateTimeFormatter.ofPattern("LLLL", Locale("ru")))
                }
            }
        }
    }
}
