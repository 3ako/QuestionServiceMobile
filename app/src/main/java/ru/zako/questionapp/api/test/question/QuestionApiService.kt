package ru.zako.questionapp.api.test.question

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.zako.questionapp.api.AbstractApiResponse
import ru.zako.questionapp.user.UserDto


internal interface QuestionApiService {
    @POST("/getall")
    suspend fun register(@Body user: UserDto): Response<AbstractApiResponse<Any>>

}
