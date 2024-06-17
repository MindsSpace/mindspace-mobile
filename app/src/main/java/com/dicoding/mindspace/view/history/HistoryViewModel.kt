package com.dicoding.mindspace.view.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mindspace.data.remote.api.GetAllRoomsResponse
import com.dicoding.mindspace.data.repository.ChatRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: ChatRepository) : ViewModel() {
    private val _historyData = MutableLiveData<GetAllRoomsResponse>()
    val historyData: LiveData<GetAllRoomsResponse> = _historyData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _responseMessage = MutableLiveData<String>()
    val responseMessage: LiveData<String> = _responseMessage

    fun getAllRooms() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getAllRooms()
                if (response.success == false) {
                    _responseMessage.value = response.message ?: "Couldn't retrieve history"
                } else {
                    _historyData.value = response
                }
            } catch (e: Exception) {
                _responseMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}