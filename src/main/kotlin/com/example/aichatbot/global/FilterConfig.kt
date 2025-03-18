package com.example.aichatbot.global

import AuthenticationFilter
import com.example.aichatbot.auth.TokenService
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig(private val tokenService: TokenService) {
    @Bean
    fun jwtAuthenticationFilter(): FilterRegistrationBean<AuthenticationFilter> {
        val filterRegistrationBean = FilterRegistrationBean(AuthenticationFilter(tokenService))
        filterRegistrationBean.addUrlPatterns("/api/*")
        filterRegistrationBean.order = 1
        return filterRegistrationBean
    }
}