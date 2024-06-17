package com.dicoding.mindspace.view.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mindspace.data.remote.api.ProfilingResponse
import com.dicoding.mindspace.data.remote.schema.ProfilingRequest
import com.dicoding.mindspace.data.repository.ChatRepository
import kotlinx.coroutines.launch

class ProfilingViewModel(private val repository: ChatRepository): ViewModel() {
    private val _profilingData = MutableLiveData<ProfilingResponse>()
    val profilingData: LiveData<ProfilingResponse> = _profilingData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _responseMessage = MutableLiveData<String>()
    val responseMessage: LiveData<String> = _responseMessage

    fun createProfile(request: ProfilingRequest) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.createProfiling(request)
                if (response.success == false) {
                    _responseMessage.value = response.message ?: "Sorry, profiling failed"
                } else {
                    _profilingData.value = response
                }
            } catch (e: Exception) {
                _responseMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}