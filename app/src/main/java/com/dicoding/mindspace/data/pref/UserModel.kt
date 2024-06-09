package com.dicoding.mindspace.data.pref

data class UserModel(
    val nickname: String,
    val token: String,
    val isLogin: Boolean = false
)
