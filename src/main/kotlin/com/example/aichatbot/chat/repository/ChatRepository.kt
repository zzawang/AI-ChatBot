package com.example.aichatbot.chat.repository

import com.example.aichatbot.chat.entity.Chat
import com.example.aichatbot.chat.entity.Thread
import com.example.aichatbot.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ChatRepository : JpaRepository<Chat, Long> {
    fun findTopByUserOrderByCreatedAtDesc(user: User): Chat?
    fun findByThread(thread: Thread, pageable: Pageable): Page<Chat>
    fun findByUser(user: User, pageable: Pageable): Page<Chat>
}
