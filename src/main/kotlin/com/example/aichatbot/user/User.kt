package com.example.aichatbot.user

import com.example.aichatbot.BaseEntity
import com.example.aichatbot.chat.entity.Thread
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(unique = true)
    val email: String,

    val password: String,

    val name: String,

    @Enumerated(EnumType.STRING)
    val role: Role,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val threads: List<Thread> = mutableListOf()

) : BaseEntity()