package com.dicoding.mindspace.data.remote.schema

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    @SerializedName("id")
    var id: String? = null,

    @SerializedName("content")
    var content: String? = null,

    @SerializedName("is_user")
    var is_user: Boolean? = null,

    @SerializedName("room_id")
    var room_id: String? = null,

    @SerializedName("created_at")
    var created_at: String? = null,

    var tempId: String? = null,
    var isLoading: Boolean = false
): Parcelable

data class CreateChatRequest(
    @SerializedName("content")
    val content: String? = null,

    @SerializedName("room_id")
    val room_id: String? = null,
)

@Parcelize
data class Room(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("user_id")
    val user_id: String? = null,

    @SerializedName("created_at")
    val created_at: String? = null,

    @SerializedName("chats")
    val chats: List<Chat>? = null
): Parcelable