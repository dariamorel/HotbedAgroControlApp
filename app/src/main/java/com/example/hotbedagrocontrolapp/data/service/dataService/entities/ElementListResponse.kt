package com.example.hotbedagrocontrolapp.data.service.dataService.entities

import com.google.gson.annotations.SerializedName

data class ElementListResponse(
    @SerializedName("content")
    val content: List<ElementResponse>,

    @SerializedName("totalElements")
    val totalElements: Long
)