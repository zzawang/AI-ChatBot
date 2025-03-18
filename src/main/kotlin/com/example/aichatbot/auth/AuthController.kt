package com.example.aichatbot.auth

import com.example.aichatbot.user.service.UserService
import com.example.aichatbot.user.dto.UserLoginRequest
import com.example.aichatbot.user.dto.UserRegisterRequest
import com.example.aichatbot.user.dto.UserRegisterResponse
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(@Autowired private val userService: UserService) {
    @PostMapping("/register")
    fun register(@Valid @RequestBody userRegisterRequest: UserRegisterRequest): ResponseEntity<UserRegisterResponse> {
        val createdUserResponse = userService.registerUser(userRegisterRequest)
        return ResponseEntity(createdUserResponse, HttpStatus.CREATED)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody userLoginRequest: UserLoginRequest): ResponseEntity<String> {
        val jwtToken = userService.loginUser(userLoginRequest)
        return ResponseEntity(jwtToken, HttpStatus.OK)
    }
}

