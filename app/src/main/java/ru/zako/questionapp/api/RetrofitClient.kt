package ru.zako.questionapp.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.zako.questionapp.api.auth.AuthApiService
import ru.zako.questionapp.api.auth.AuthInterceptor
import ru.zako.questionapp.api.auth.ProfileService
import ru.zako.questionapp.api.test.TestApiService
import ru.zako.questionapp.api.test.question.QuestionApiService
import ru.zako.questionapp.api.test.question.answer.AnswerApiService

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.101:8080"

    private val publicApi: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val privateApi: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor {
                        TokenManager.getToken()
                    })
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApiService by lazy {
        publicApi.create(AuthApiService::class.java)
    }

    val profileApi: ProfileService by lazy {
        privateApi.create(ProfileService::class.java)
    }

    val questionApi: QuestionApiService by lazy {
        privateApi.create(QuestionApiService::class.java)
    }

    val answerApi: AnswerApiService by lazy {
        privateApi.create(AnswerApiService::class.java)
    }

    val testApi: TestApiService by lazy {
        privateApi.create(TestApiService::class.java)
    }
}