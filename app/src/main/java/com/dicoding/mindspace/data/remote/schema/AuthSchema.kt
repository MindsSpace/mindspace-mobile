package com.dicoding.mindspace.data.remote.schema

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

typealias RegisterResponse = ApiResponse<RegisterData>

data class RegisterRequest(
    val nickname: String,
    val pin: String
)

@Parcelize
data class RegisterData(
    @SerializedName("token")
    val token: String? = null
): Parcelable