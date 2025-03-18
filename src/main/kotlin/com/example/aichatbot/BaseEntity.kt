package com.example.aichatbot

import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
class BaseEntity(
    val createdAt: LocalDateTime = LocalDateTime.now()
)