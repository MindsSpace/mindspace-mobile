package com.dicoding.mindspace.view.start

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.mindspace.data.pref.UserModel
import com.dicoding.mindspace.data.remote.api.ApiConfig
import com.dicoding.mindspace.data.remote.api.GetMeResponse
import com.dicoding.mindspace.data.remote.api.RegisterResponse
import com.dicoding.mindspace.data.remote.schema.RegisterRequest
import com.dicoding.mindspace.data.repository.UserRepository
import kotlinx.coroutines.launch

class GreetingViewModel(private val repository: UserRepository) : ViewModel() {
    private val _registerData = MutableLiveData<RegisterResponse>()
    val registerData: LiveData<RegisterResponse> = _registerData

    private val _meData = MutableLiveData<GetMeResponse>()
    val meData: LiveData<GetMeResponse> = _meData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _responseMessage = MutableLiveData<String>()
    val responseMessage: LiveData<String> = _responseMessage

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getMe(token: String?) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService(token).getMe()
                if(response.success == true) {
                    _meData.value = response
                    saveSession(UserModel(
                        response.data?.username!!,
                        token!!,
                        true,
                        response.data.is_profiled,
                        response.data.level,
                        response.data.point,
                        response.data.avatar
                    ))
                } else {
                    _responseMessage.value = response.message ?: "Get me failed"
                }
            } catch (e: Exception) {
                _responseMessage.value = e.message
            }
        }
    }

    fun register(nickname: String, pin: String) {
        _isLoading.value = true
        val request = RegisterRequest(nickname, pin)

        viewModelScope.launch {
            try {
                val response = repository.register(request)
                if (response.success == false) {
                    _responseMessage.value = response.message ?: "Sorry, registration failed"
                } else {
                    _registerData.value = response
                }
            } catch (e: Exception) {
                _responseMessage.value = e.message
            }
        }
    }
}