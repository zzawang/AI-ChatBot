package com.example.aichatbot.chat.repository

import com.example.aichatbot.chat.entity.Thread
import com.example.aichatbot.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ThreadRepository : JpaRepository<Thread, Long> {
    fun findByUser(user: User, pageable: Pageable): Page<Thread>
    fun findAllBy(pageable: Pageable): Page<Thread>
}
