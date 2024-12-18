package ru.zako.questionapp.api.test.question.answer

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.zako.questionapp.api.AbstractApiResponse

interface AnswerApiService {
    @GET("/test/question/answer/getall")
    suspend fun getAll(@Query("questionId") questionId: Long): Response<AbstractApiResponse<List<AnswerDTO>>>

    @POST("/test/question/answer/create")
    suspend fun create(@Body request: CreateAnswerRequest): Response<AbstractApiResponse<AnswerDTO>>
}
