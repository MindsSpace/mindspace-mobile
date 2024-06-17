package com.dicoding.mindspace.data.remote.schema

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class Journal(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("content")
    val content: String? = null,

    @SerializedName("image")
    val image: String? = null,

    @SerializedName("created_at")
    val created_at: String? = null,
): Parcelable

@Parcelize data class CreateJournalRequest(
    @SerializedName("content")
    val content: String? = null,

    @SerializedName("image")
    val image: File? = null,
): Parcelable