package com.example.hotbedagrocontrolapp.domain.entities

interface Element {
    val topic: String
}

enum class Sensor(override val topic: String): Element {
    AIR_HUMIDITY("HBed_agr_h"), AIR_TEMPERATURE("HBed_agr_t"),
    FLUID_TEMPERATURE("HBed_agr_tds"), FLUID_LEVEL("HBed_agr_lv"),
    EC("HBed_agr_ec"), LUX("HBed_agr_l"), PH("HBed_agr_ph")
}

enum class Control(override val topic: String): Element {
    CLEAR_CLOUDY("ClearCloudy"), RELAY_1("relay1"),
    RELAY_2("relay2"), RELAY_3("relay3"),
    IF_EC("IFEC"), IF_PH("IFPH");
}