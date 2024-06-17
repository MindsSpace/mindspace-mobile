package com.dicoding.mindspace.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mindspace.data.remote.api.GetWeeklyStreakResponse
import com.dicoding.mindspace.data.repository.ChatRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ChatRepository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _weeklyStreakData = MutableLiveData<GetWeeklyStreakResponse>()
    val weeklyStreakData: LiveData<GetWeeklyStreakResponse> = _weeklyStreakData

    fun getWeeklyStreak() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getWeeklyStreak()
                if (response.success == true) {
                    _weeklyStreakData.value = response
                }
            } catch (e: Exception) {
                Log.e("Error Weekly Streak Data Fetch", e.message.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }
}