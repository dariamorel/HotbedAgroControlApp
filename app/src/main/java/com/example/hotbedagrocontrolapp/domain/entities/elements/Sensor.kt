package com.example.hotbedagrocontrolapp.domain.entities.elements

import com.example.hotbedagrocontrolapp.R
import com.example.hotbedagrocontrolapp.domain.entities.elements.Element
import com.example.hotbedagrocontrolapp.ui.theme.BottlePurple
import com.example.hotbedagrocontrolapp.ui.theme.SunYellow
import com.example.hotbedagrocontrolapp.ui.theme.WaterBlue

enum class Sensor(
    override val topic: String,
    override val elementName: String,
    override val iconInfo: IconInfo,
    val minValue: Double,
    val maxValue: Double,
    val units: String
) : Element {
    AIR_HUMIDITY(
        "HBed_agr_h",
        "Влажность воздуха",
        IconInfo(R.drawable.air_humidity, WaterBlue),
        minValue = 0.0,
        maxValue = 100.0,
        units = "%"
    ),
    AIR_TEMPERATURE(
        "HBed_agr_t",
        "Температура воздуха",
        IconInfo(R.drawable.air_temperature, BottlePurple),
        minValue = -20.0,
        maxValue = 70.0,
        units = "°C"
    ),
    FLUID_TEMPERATURE(
        "HBed_agr_tds",
        "Температура раствора",
        IconInfo(R.drawable.fluid_temperature, WaterBlue),
        minValue = 0.0,
        maxValue = 80.0,
        units = "°C"
    ),
    FLUID_LEVEL(
        "HBed_agr_lv",
        "Уровень жидкости",
        IconInfo(R.drawable.fluid_level, WaterBlue),
        minValue = 0.0,
        maxValue = 100.0,
        units = "см"
    ),
    EC(
        "HBed_agr_ec",
        "EC",
        IconInfo(R.drawable.ec, SunYellow),
        minValue = 0.0,
        maxValue = 5.0,
        units = "мСм/см"
    ),
    LUX(
        "HBed_agr_l",
        "Lux",
        IconInfo(R.drawable.sun, SunYellow),
        minValue = 0.0,
        maxValue = 100000.0,
        units = "люкс"
    ),
    PH(
        "HBed_agr_ph",
        "PH",
        IconInfo(R.drawable.ph, BottlePurple),
        minValue = 0.0,
        maxValue = 14.0,
        units = "ед."
    )
}