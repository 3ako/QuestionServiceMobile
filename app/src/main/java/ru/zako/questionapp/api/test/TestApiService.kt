package ru.zako.questionapp.api.test

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.zako.questionapp.api.AbstractApiResponse

interface TestApiService {
    @GET("/test/getall")
    suspend fun getAll(): Response<AbstractApiResponse<List<TestDTO>>>
    @POST("/test/create")
    suspend fun create(@Body request: CreateTestRequest): Response<AbstractApiResponse<TestDTO>>
}