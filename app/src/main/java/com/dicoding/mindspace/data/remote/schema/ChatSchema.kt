package com.dicoding.mindspace.data.remote.schema

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class Message(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("text")
    val text: String? = null,

    @SerializedName("sender")
    val name: String? = null,

    @SerializedName("timestamp")
    val timestamp: Long? = null,

    @SerializedName("session_id")
    val session_id: String? = null
)