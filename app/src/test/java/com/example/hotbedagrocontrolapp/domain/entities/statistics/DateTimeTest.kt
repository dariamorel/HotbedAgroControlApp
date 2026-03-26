package com.example.hotbedagrocontrolapp.domain.entities.statistics

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class DateTimeTest {

    @Test
    fun `day constructor resets time to start of day`() {
        val dateTime = DateTime(
            AnaliseType.DAY,
            LocalDateTime.of(2026, 3, 22, 19, 57, 9)
        )

        assertEquals(
            LocalDateTime.of(2026, 3, 22, 0, 0),
            dateTime.localDateTime
        )
    }

    @Test
    fun `plus for hour shifts by hours`() {
        val dateTime = DateTime(
            AnaliseType.HOUR,
            LocalDateTime.of(2026, 3, 22, 19, 57)
        )

        assertEquals(
            LocalDateTime.of(2026, 3, 22, 21, 0),
            dateTime.plus(2).localDateTime
        )
    }

    @Test
    fun `iterator for day moves by hours`() {
        val dateTime = DateTime(
            AnaliseType.DAY,
            LocalDateTime.of(2026, 3, 22, 19, 57)
        )

        assertEquals(
            LocalDateTime.of(2026, 3, 22, 3, 0),
            dateTime.iterator.plus(3)
        )
    }
}
