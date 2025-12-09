package com.example.hotbedagrocontrolapp.presentation.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotbedagrocontrolapp.data.db.DataBaseManager
import com.example.hotbedagrocontrolapp.data.db.DataBaseManager.Companion.DATA_BASE_TAG
import com.example.hotbedagrocontrolapp.domain.entities.Control
import com.example.hotbedagrocontrolapp.domain.entities.ControlResponse
import com.example.hotbedagrocontrolapp.domain.entities.Element
import com.example.hotbedagrocontrolapp.domain.entities.Response
import com.example.hotbedagrocontrolapp.domain.interfaces.Client.Companion.CLIENT_TAG
import com.example.hotbedagrocontrolapp.domain.interfaces.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AgroControlViewModel(
    private val repository: Repository
) : ViewModel() {
    private val _currentData = MutableStateFlow<MutableMap<Element, Response>>(mutableMapOf())
    val currentData = _currentData.asStateFlow()

    private val _dataHistory =
        MutableStateFlow<Map<LocalDateTime, Map<Element, Response>>>(emptyMap())
    val dataHistory = _dataHistory.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertCurrentData(element: Element, response: Response) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentTime = LocalDateTime.now()
                repository.insertData(element, response, currentTime)
            } catch (e: Exception) {
                Log.e(DATA_BASE_TAG, "Error while inserting new data in db: ${e.message}.")
            }
        }
    }

    fun publish(control: Control, status: ControlResponse.Status) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.publish(control, status)
            } catch (e: Exception) {
                Log.e(CLIENT_TAG, "Error while publishing data: ${e.message}.")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onMessageReceived(element: Element, response: Response) {
        _currentData.value[element] = response
        insertCurrentData(element, response)
    }
}