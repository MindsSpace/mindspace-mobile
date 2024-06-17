package com.dicoding.mindspace.data.pref

data class UserModel(
    val username: String,
    val token: String,
    val isLogin: Boolean = false,
    val is_profiled: Boolean?,
    val level: Int?,
    val point: Int?,
    val avatar: String?
)
