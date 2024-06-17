package com.dicoding.mindspace.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.mindspace.data.pref.UserModel
import com.dicoding.mindspace.data.remote.api.ApiConfig
import com.dicoding.mindspace.data.remote.api.CreateRoomResponse
import com.dicoding.mindspace.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _roomData = MutableLiveData<CreateRoomResponse>()
    val roomData: LiveData<CreateRoomResponse> = _roomData

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun createRoom() {
        viewModelScope.launch {
            try {
                val token = userRepository.getToken()
                val response = ApiConfig.getApiService(token).createRoom()
                if (response.success == true) {
                    _roomData.value = response
                }
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }
}