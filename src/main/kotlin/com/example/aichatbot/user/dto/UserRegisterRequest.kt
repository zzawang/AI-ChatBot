package com.example.aichatbot.user.dto

import com.example.aichatbot.user.Role
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Null
import jakarta.validation.constraints.Size

data class UserRegisterRequest(
    @field:NotBlank(message = "이메일을 입력하세요.")
    @field:Email(message = "유효하지 않은 이메일 형식입니다.")
    val email: String,

    @field:NotBlank(message = "비밀번호를 입력하세요.")
    @field:Size(min = 8, max = 20, message = "비밀번호는 8자 이상 입력해주세요.")
    val password: String,

    @field:NotBlank(message = "이름을 입력하세요.")
    val name: String,

    @Null
    val role: Role? = null
)