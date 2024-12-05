package ru.zako.questionapp.api.auth

import retrofit2.Response
import retrofit2.http.GET
import ru.zako.questionapp.api.AbstractApiResponse
import ru.zako.questionapp.profile.ProfileDTO

interface ProfileService {
    @GET("/profile")
    suspend fun getProfile(): Response<AbstractApiResponse<ProfileDTO>>
}