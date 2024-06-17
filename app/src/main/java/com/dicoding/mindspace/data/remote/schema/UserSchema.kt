package com.dicoding.mindspace.data.remote.schema

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterRequest(
    val username: String,
    val password: String
): Parcelable

@Parcelize
data class RegisterData(
    @SerializedName("token")
    val token: String? = null,

    @SerializedName("is_profiled")
    val is_profiled: Boolean? = null
): Parcelable

@Parcelize
data class GetMeData(
    @SerializedName("username")
    val username: String? = null,

    @SerializedName("level")
    val level: Int? = null,

    @SerializedName("point")
    val point: Int? = null,

    @SerializedName("is_profiled")
    val is_profiled: Boolean? = null,

    @SerializedName("avatar")
    val avatar: String? = null
): Parcelable

@Parcelize
data class DailyStreakData(
    @SerializedName("is_filled")
    val is_filled: Boolean? = null,

    @SerializedName("created_at")
    val datetime: String? = null,

    @SerializedName("mood")
    val mood: String? = null,

    @SerializedName("problems")
    val problems: ArrayList<String>? = null,

    @SerializedName("approaches")
    val helps: ArrayList<String>? = null
): Parcelable

@Parcelize
data class ProfilingRequest(
    @SerializedName("mood")
    val mood: String? = null,

    @SerializedName("problems")
    val problems: ArrayList<String>? = null,

    @SerializedName("approaches")
    val helps: ArrayList<String>? = null
): Parcelable

@Parcelize
data class ProfilingData(
    @SerializedName("mood")
    val mood: String? = null,

    @SerializedName("problems")
    val problems: ArrayList<String>? = null,

    @SerializedName("approaches")
    val helps: ArrayList<String>? = null,

    @SerializedName("room_id")
    val room_id: String? = null
): Parcelable