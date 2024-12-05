package ru.zako.questionapp.api.test

import retrofit2.Response
import retrofit2.http.GET
import ru.zako.questionapp.api.AbstractApiResponse

interface TestApiService {
    @GET("/test/getall")
    suspend fun getAll(): Response<AbstractApiResponse<List<TestDTO>>>
}