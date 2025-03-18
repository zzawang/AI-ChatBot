package com.example.aichatbot.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(@Autowired private val userService: UserService) {
    @GetMapping
    fun getUsers(): List<User> { return userService.getUsers() }
}

