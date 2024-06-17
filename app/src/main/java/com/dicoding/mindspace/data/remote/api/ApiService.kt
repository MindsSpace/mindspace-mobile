package com.dicoding.mindspace.data.remote.api

import com.dicoding.mindspace.data.remote.schema.ApiResponse
import com.dicoding.mindspace.data.remote.schema.Chat
import com.dicoding.mindspace.data.remote.schema.CreateChatRequest
import com.dicoding.mindspace.data.remote.schema.CreateJournalRequest
import com.dicoding.mindspace.data.remote.schema.DailyStreakData
import com.dicoding.mindspace.data.remote.schema.GetMeData
import com.dicoding.mindspace.data.remote.schema.Journal
import com.dicoding.mindspace.data.remote.schema.ProfilingData
import com.dicoding.mindspace.data.remote.schema.ProfilingRequest
import com.dicoding.mindspace.data.remote.schema.RegisterData
import com.dicoding.mindspace.data.remote.schema.RegisterRequest
import com.dicoding.mindspace.data.remote.schema.Room
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("users")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    @GET("users/me")
    suspend fun getMe(): GetMeResponse

    @POST("chats")
    suspend fun sendChat(
        @Body request: CreateChatRequest
    ): CreateChatResponse

    @POST("rooms")
    suspend fun createRoom(): CreateRoomResponse

    @GET("rooms/{room_id}")
    suspend fun getRoomById(
        @Path("room_id") id: String,
    ): GetRoomByIdResponse

    @GET("rooms")
    suspend fun getAllRooms(): GetAllRoomsResponse

    @GET("profilings")
    suspend fun getWeeklyStreak(): GetWeeklyStreakResponse

    @POST("profilings")
    suspend fun createProfiling(
        @Body request: ProfilingRequest
    ): ProfilingResponse

    @GET("journals")
    suspend fun getAllJournals(): GetAllJournalsResponse

    @Multipart
    @POST("journals")
    suspend fun createJournal(
        @Part image: MultipartBody.Part?,
        @Part("content") content: RequestBody,
    ): AnyApiResponse

    @GET("journals/{journal_id}")
    suspend fun getJournalById(
        @Path("journal_id") id: String
    ): GetJournalByIdResponse

    @DELETE("journals/{journal_id}")
    suspend fun deleteJournal(
        @Path("journal_id") id: String
    ): AnyApiResponse
}

typealias RegisterResponse = ApiResponse<RegisterData>
typealias GetMeResponse = ApiResponse<GetMeData>
typealias CreateChatResponse = ApiResponse<Chat>
typealias GetRoomByIdResponse = ApiResponse<Room>
typealias GetAllRoomsResponse = ApiResponse<List<Room>>
typealias GetWeeklyStreakResponse = ApiResponse<List<DailyStreakData>>
typealias CreateRoomResponse = ApiResponse<Room>
typealias ProfilingResponse = ApiResponse<ProfilingData>
typealias GetAllJournalsResponse = ApiResponse<List<Journal>>
typealias GetJournalByIdResponse = ApiResponse<Journal>
typealias AnyApiResponse = ApiResponse<Any>