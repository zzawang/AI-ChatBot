package com.example.aichatbot.chat.service

import com.example.aichatbot.chat.dto.ChatCreateRequest
import com.example.aichatbot.chat.dto.ChatDTO
import com.example.aichatbot.chat.dto.Message
import com.example.aichatbot.chat.dto.ThreadDTO
import com.example.aichatbot.chat.entity.Chat
import com.example.aichatbot.chat.entity.Thread
import com.example.aichatbot.chat.repository.ChatRepository
import com.example.aichatbot.chat.repository.ThreadRepository
import com.example.aichatbot.user.Role
import com.example.aichatbot.user.User
import com.example.aichatbot.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

@Service
class ChatService(
    @Autowired private val webClient: WebClient.Builder,
    @Autowired private val chatRepository: ChatRepository,
    @Autowired private val threadRepository: ThreadRepository,
    @Autowired private val userRepository: UserRepository
) {
    @Value("\${api-key}")
    private lateinit var apiKey:String

    fun generateResponse(messages: List<Message>, model: String = "gpt-3.5-turbo", isStreaming: Boolean = false): Mono<String> {
        return webClient.baseUrl("https://api.openai.com/v1/chat/completions")
            .defaultHeader("Authorization", "Bearer $apiKey")
            .build()
            .post()
            .bodyValue(
                mapOf(
                    "model" to model,
                    "messages" to messages,
                    "stream" to isStreaming
                )
            )
            .retrieve()
            .bodyToMono(Map::class.java)
            .map { it["choices"] as List<Map<String, Any>> }
            .flatMap {
                Mono.just(it[0]["message"]?.toString() ?: "응답할 수 없는 질문입니다.")
            }
    }

    fun createChat(userId: Long, request: ChatCreateRequest): ChatDTO {
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("존재하지 않는 사용자입니다.") }
        val lastChat = chatRepository.findTopByUserOrderByCreatedAtDesc(user)
        val thread = getOrCreateThread(user, lastChat)

        val messages = listOf(Message(role = "user", content = request.question))
        val answer = generateResponse(messages, model = request.model, isStreaming = request.isStreaming).block() ?: "응답이 존재하지 않습니다."

        val chat = Chat(
            question = request.question,
            answer = answer,
            user = user,
            thread = thread
        )

        val savedChat = chatRepository.save(chat)
        return ChatDTO(savedChat.question, savedChat.answer)
    }

    private fun getOrCreateThread(user: User, lastChat: Chat?): Thread {
        val currentTime = LocalDateTime.now()
        return if (lastChat != null && currentTime.minusMinutes(30).isBefore(lastChat.createdAt)) {
            lastChat.thread
        } else {
            Thread(user = user).apply { threadRepository.save(this) }
        }
    }

    fun getChatList(userId: Long, page: Int, size: Int, sortDirection: String, sortBy: String): List<ThreadDTO> {
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("존재하지 않는 사용자입니다.") }

        val sort = if (sortDirection == "DESC") Sort.by(sortBy).descending() else Sort.by(sortBy).ascending()
        val pageable: Pageable = PageRequest.of(page, size, sort)

        val threads = if (user.role == Role.ADMIN) {
            threadRepository.findAll(pageable).content
        } else {
            threadRepository.findByUser(user, pageable).content
        }

        return threads.map { thread ->
            val chats = chatRepository.findByThread(thread, pageable).content
            ThreadDTO(
                id = thread.id,
                chats = chats.map { chat ->
                    ChatDTO(question = chat.question, answer = chat.answer)
                }
            )
        }
    }

    fun deleteThread(userId: Long, threadId: Long) {
        val thread = threadRepository.findById(threadId).orElseThrow { IllegalArgumentException("존재하지 않는 스레드입니다.") }

        if (thread.user.id != userId) {
            throw IllegalArgumentException("스레드 소유주만 삭제할 수 있습니다.")
        }

        threadRepository.delete(thread)
    }
}