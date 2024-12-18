package ru.zako.questionapp.api.test.question

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.zako.questionapp.api.AbstractApiResponse


interface QuestionApiService {
    @GET("/test/question/getAll")
    suspend fun getAll(@Query("testId") testId: Long): Response<AbstractApiResponse<List<QuestionDTO>>>
    @POST("/test/question/create")
    suspend fun create(@Body request: CreateQuestionRequest): Response<AbstractApiResponse<QuestionDTO>>
}
