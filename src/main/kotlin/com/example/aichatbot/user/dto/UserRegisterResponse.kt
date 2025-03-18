package com.example.aichatbot.user.dto

import com.example.aichatbot.user.Role

data class UserRegisterResponse(
    val id: Long,
    val email: String,
    val name: String,
    val role: Role
)