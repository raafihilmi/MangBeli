package com.bumantra.mangbeli.model

data class User(
    val token: String,
    val email: String,
    val isLogin: Boolean = false
)
