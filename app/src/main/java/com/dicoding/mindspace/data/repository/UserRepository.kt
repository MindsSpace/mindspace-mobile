package com.dicoding.mindspace.data.repository

import com.dicoding.mindspace.data.pref.UserModel
import com.dicoding.mindspace.data.pref.UserPreference
import com.dicoding.mindspace.data.remote.api.ApiService
import com.dicoding.mindspace.data.remote.schema.ApiResponse
import com.dicoding.mindspace.data.remote.schema.RegisterData
import com.dicoding.mindspace.data.remote.schema.RegisterRequest
import com.dicoding.mindspace.data.remote.schema.RegisterResponse
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun getToken(): String {
        return userPreference.getToken()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun register(request: RegisterRequest): RegisterResponse {
        return apiService.register(request)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}