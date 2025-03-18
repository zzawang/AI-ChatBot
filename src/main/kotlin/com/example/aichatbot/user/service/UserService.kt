package com.example.aichatbot.user.service

import com.example.aichatbot.auth.TokenService
import com.example.aichatbot.user.repository.UserRepository
import com.example.aichatbot.user.dto.UserLoginRequest
import com.example.aichatbot.user.dto.UserRegisterRequest
import com.example.aichatbot.user.dto.UserRegisterResponse
import com.example.aichatbot.user.entity.Role
import com.example.aichatbot.user.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(@Autowired private val userRepository: UserRepository, @Autowired private val tokenService: TokenService) {
    fun getUsers(): List<User> { return userRepository.findAll().toList() }

    fun registerUser(request: UserRegisterRequest): UserRegisterResponse {
        // null인 경우 기본값으로 MEMBER로 설정
        val role = request.role ?: Role.MEMBER

        // 역할 검증
        if (role !in Role.entries.toTypedArray()) {
            throw IllegalArgumentException("이미 존재하는 이메일 주소입니다.")
        }

        // 이메일 중복 체크
        if (userRepository.findByEmail(request.email).isPresent) {
            throw IllegalArgumentException("이미 등록된 사용자입니다.")
        }

        val user = User(
            id = 0,
            email = request.email,
            password = BCryptPasswordEncoder().encode(request.password),
            name = request.name,
            role = request.role ?: Role.MEMBER
        )

        val createdUser = userRepository.save(user)

        return UserRegisterResponse(
            id = createdUser.id,
            email = createdUser.email,
            name = createdUser.name,
            role = createdUser.role
        )
    }

    fun loginUser(userLoginRequest: UserLoginRequest): String {
        val user = userRepository.findByEmail(userLoginRequest.email).orElseThrow { IllegalArgumentException("존재하지 않는 사용자입니다.") }
        if (!BCryptPasswordEncoder().matches(userLoginRequest.password, user.password)) {
            throw IllegalArgumentException("존재하지 않는 사용자입니다.")
        }
        return tokenService.generate(user)
    }
}