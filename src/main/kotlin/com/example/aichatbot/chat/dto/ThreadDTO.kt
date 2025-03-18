package com.example.aichatbot.chat.dto

data class ThreadDTO (
    val id: Long? = null,
    val chats: List<ChatDTO>
)