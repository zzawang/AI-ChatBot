package com.example.aichatbot.chat.entity

import com.example.aichatbot.BaseEntity
import com.example.aichatbot.user.entity.User
import jakarta.persistence.*

@Entity
data class Thread(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @OneToMany(mappedBy = "thread", cascade = [CascadeType.ALL], orphanRemoval = true)
    val chats: List<Chat> = mutableListOf()

) : BaseEntity()