package com.example.hotbedagrocontrolapp.domain.entities.elements

import androidx.compose.ui.graphics.Color
import com.example.hotbedagrocontrolapp.R
import com.example.hotbedagrocontrolapp.domain.entities.elements.Element
import com.example.hotbedagrocontrolapp.ui.theme.BottlePurple
import com.example.hotbedagrocontrolapp.ui.theme.SunYellow

enum class Control(
    override val topic: String,
    override val elementName: String,
    override val iconInfo: IconInfo
) : Element {
    CLEAR_CLOUDY(
        "ClearCloudy",
        "Ясно/Пасмурно",
        IconInfo(R.drawable.sun, SunYellow)
    ),
    RELAY_1(
        "relay1",
        "Реле 1",
        IconInfo(R.drawable.relay, Color.DarkGray)
    ),
    RELAY_2(
        "relay2",
        "Реле 2",
        IconInfo(R.drawable.relay, Color.DarkGray)
    ),
    RELAY_3("relay3",
        "Реле 3",
        IconInfo(R.drawable.relay, Color.DarkGray)
    ),
    IF_EC("IFEC",
        "Вкл/Выкл\nEC",
        IconInfo(R.drawable.ec, SunYellow)
    ),
    IF_PH("IFPH",
        "Вкл/Выкл\nPH",
        IconInfo(R.drawable.ph, BottlePurple)
    );
}

data class IconInfo(
    val resourceId: Int,
    val tint: Color
)