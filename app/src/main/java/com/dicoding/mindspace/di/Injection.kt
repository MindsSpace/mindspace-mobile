package com.dicoding.mindspace.di

import android.content.Context
import com.dicoding.mindspace.data.pref.UserPreference
import com.dicoding.mindspace.data.pref.dataStore
import com.dicoding.mindspace.data.remote.api.ApiConfig
import com.dicoding.mindspace.data.repository.ChatRepository
import com.dicoding.mindspace.data.repository.UserRepository
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(null)
        return UserRepository.getInstance(pref, apiService)
    }

    fun provideChatRepository(context: Context): ChatRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val token = runBlocking { pref.getToken() }
        val apiService = ApiConfig.getApiService(token)
        return ChatRepository.getInstance(apiService)
    }
}