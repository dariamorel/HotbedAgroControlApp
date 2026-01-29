package com.example.hotbedagrocontrolapp.domain.interfaces.entities.elements

import com.example.hotbedagrocontrolapp.domain.entities.elements.IconInfo


/**
 * Элемент на устройстве.
 */
interface Element {
    /**
     * Топик в Mosquitto.
     */
    val topic: String

    /**
     * Название элемента.
     */
    val elementName: String

    /**
     * Информация об иконке.
     */
    val iconInfo: IconInfo
}