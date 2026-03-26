package com.example.hotbedagrocontrolapp.domain.entities.elements

import org.junit.Assert.assertEquals
import org.junit.Test

class ResponseTest {

    @Test
    fun `sensor response converts to string and double`() {
        val response = SensorResponse(24.5)

        assertEquals("24.5", response.dataToString)
        assertEquals(24.5, response.dataToDouble, 0.0)
    }

    @Test
    fun `control response on converts to one`() {
        val response = ControlResponse(ControlResponse.Status.ON)

        assertEquals("ON", response.dataToString)
        assertEquals(1.0, response.dataToDouble, 0.0)
    }

    @Test
    fun `control response off converts to zero`() {
        val response = ControlResponse(ControlResponse.Status.OFF)

        assertEquals("OFF", response.dataToString)
        assertEquals(0.0, response.dataToDouble, 0.0)
    }
}
