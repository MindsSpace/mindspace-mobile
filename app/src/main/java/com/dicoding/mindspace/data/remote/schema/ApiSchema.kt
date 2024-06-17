package com.dicoding.mindspace.data.remote.schema

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: T? = null
)