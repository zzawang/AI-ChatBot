package com.example.aichatbot.chat.controller

import com.example.aichatbot.chat.dto.ChatCreateRequest
import com.example.aichatbot.chat.dto.ChatDTO
import com.example.aichatbot.chat.dto.ThreadDTO
import com.example.aichatbot.chat.service.ChatService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chat")
class ChatController(
    @Autowired private val chatService: ChatService
) {
    @GetMapping("/list")
    fun getChatList(
        @RequestParam userId: Long,
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        @RequestParam sortDirection: String = "ASC",
        @RequestParam sortBy: String = "createdAt"
    ): List<ThreadDTO> {
        return chatService.getChatList(userId, page, size, sortDirection, sortBy)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createChat(@RequestParam userId: Long, @RequestBody request: ChatCreateRequest): ChatDTO {
        return chatService.createChat(userId, request)
    }

    @DeleteMapping("/thread/{threadId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteThread(@RequestParam userId: Long, @PathVariable threadId: Long) {
        chatService.deleteThread(userId, threadId)
    }
}