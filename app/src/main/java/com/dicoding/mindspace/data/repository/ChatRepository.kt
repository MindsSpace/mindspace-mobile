package com.dicoding.mindspace.data.repository

import com.dicoding.mindspace.data.remote.api.AnyApiResponse
import com.dicoding.mindspace.data.remote.api.ApiService
import com.dicoding.mindspace.data.remote.api.CreateChatResponse
import com.dicoding.mindspace.data.remote.api.GetAllJournalsResponse
import com.dicoding.mindspace.data.remote.api.GetAllRoomsResponse
import com.dicoding.mindspace.data.remote.api.GetJournalByIdResponse
import com.dicoding.mindspace.data.remote.api.GetRoomByIdResponse
import com.dicoding.mindspace.data.remote.api.GetWeeklyStreakResponse
import com.dicoding.mindspace.data.remote.api.ProfilingResponse
import com.dicoding.mindspace.data.remote.schema.CreateChatRequest
import com.dicoding.mindspace.data.remote.schema.CreateJournalRequest
import com.dicoding.mindspace.data.remote.schema.ProfilingRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ChatRepository private constructor(
    private val apiService: ApiService
) {

    suspend fun sendChat(request: CreateChatRequest): CreateChatResponse {
        return apiService.sendChat(request)
    }

    suspend fun getRoomById(id: String): GetRoomByIdResponse {
        return apiService.getRoomById(id)
    }

    suspend fun getAllRooms(): GetAllRoomsResponse {
        return apiService.getAllRooms()
    }

    suspend fun getWeeklyStreak(): GetWeeklyStreakResponse {
        return apiService.getWeeklyStreak()
    }

    suspend fun createProfiling(request: ProfilingRequest): ProfilingResponse {
        return apiService.createProfiling(request)
    }

    suspend fun createJournal(image: MultipartBody.Part?, content: RequestBody): AnyApiResponse {
        return apiService.createJournal(image, content)
    }

    suspend fun getAllJournals(): GetAllJournalsResponse {
        return apiService.getAllJournals()
    }

    suspend fun getJournalById(id: String): GetJournalByIdResponse {
        return apiService.getJournalById(id)
    }

    suspend fun deleteJournal(id: String): AnyApiResponse {
        return apiService.deleteJournal(id)
    }

    companion object {
        fun getInstance(apiService: ApiService): ChatRepository = ChatRepository(apiService)
    }
}