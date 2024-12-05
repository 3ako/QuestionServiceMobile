package ru.zako.questionapp.api.auth

data class AuthRequest(
    val username: String,
    val password: String
)