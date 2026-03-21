package com.example.hotbedagrocontrolapp.data.repository

import com.example.hotbedagrocontrolapp.data.db.DataBaseManager
import com.example.hotbedagrocontrolapp.data.db.HBedEntity
import com.example.hotbedagrocontrolapp.data.service.dataService.DataServiceManager
import com.example.hotbedagrocontrolapp.data.service.dataService.entities.ElementResponse
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val dataBaseManager: DataBaseManager,
    private val dataServiceManager: DataServiceManager
) {
    suspend fun getDataHistory()

    private suspend fun addNewDataToDb(elements: List<ElementResponse>) {
        for (response in elements) {
            dataBaseManager.insertData(
                HBedEntity(
                    time = response.time,
                    element = response.element,
                    response = response.response
                )
            )
        }
    }
}