package com.dicoding.mindspace.data.remote.api

import com.dicoding.mindspace.data.remote.schema.RegisterRequest
import com.dicoding.mindspace.data.remote.schema.RegisterResponse
import retrofit2.http.*

interface ApiService {
    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse
}