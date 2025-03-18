package com.example.aichatbot.auth

import com.example.aichatbot.user.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService {
    @Value("\${jwt.secret-key}")
    private lateinit var secretKey: String

    @Value("\${jwt.expiration-time}")
    private var expirationTime: Long = 1000 * 60 * 60 * 24

    fun generate(user: User): String {
        return Jwts.builder()
            .setSubject(user.id.toString())
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun validate(token: String): Boolean {
        val claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
        return claims != null
    }
}