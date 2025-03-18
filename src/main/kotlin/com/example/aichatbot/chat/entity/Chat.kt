package com.example.aichatbot.chat.entity

import com.example.aichatbot.BaseEntity
import com.example.aichatbot.user.entity.User
import jakarta.persistence.*

@Entity
data class Chat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val question: String,

    val answer: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id")
    val thread: Thread

) : BaseEntity()