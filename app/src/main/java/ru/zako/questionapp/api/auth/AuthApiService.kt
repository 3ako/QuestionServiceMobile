package ru.zako.questionapp.api.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.zako.questionapp.api.AbstractApiResponse
import ru.zako.questionapp.user.UserDto

interface AuthApiService {
    @POST("/register")
    suspend fun register(@Body user: UserDto): Response<AbstractApiResponse<Any>>

    @POST("/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<AbstractApiResponse<String>>
}