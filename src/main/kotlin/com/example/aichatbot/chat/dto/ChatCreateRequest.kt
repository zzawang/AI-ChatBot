package com.example.aichatbot.chat.dto

data class ChatCreateRequest (
    val question: String,
    val isStreaming: Boolean = false,
    val model: String = "gpt-3.5-turbo"
)