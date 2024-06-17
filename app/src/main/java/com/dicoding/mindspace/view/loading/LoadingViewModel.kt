package com.dicoding.mindspace.view.loading

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.mindspace.data.pref.UserModel
import com.dicoding.mindspace.data.remote.api.ApiConfig
import com.dicoding.mindspace.data.remote.api.GetMeResponse
import com.dicoding.mindspace.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoadingViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _responseMessage = MutableLiveData<String>()
    val responseMessage: LiveData<String> = _responseMessage

    private val _meData = MutableLiveData<GetMeResponse>()
    val meData: LiveData<GetMeResponse> = _meData

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    init {
        _isLoading.value = true
    }

    fun getMe(token: String?) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService(token).getMe()
                if (response.success == false) {
                    _responseMessage.value = response.message ?: "Something went wrong"
                } else {

                    _meData.value = response
                }
            } catch (e: Exception) {
                _responseMessage.value = e.message ?: "Error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}