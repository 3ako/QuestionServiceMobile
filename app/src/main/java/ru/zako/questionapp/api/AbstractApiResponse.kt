package ru.zako.questionapp.api

data class AbstractApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)