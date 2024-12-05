package ru.zako.questionapp.api.auth

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val getAuthToken: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authToken = getAuthToken()

        val request = chain.request().newBuilder().apply {
            authToken?.let {
                addHeader("Authorization", it)
            }
        }.build()

        return chain.proceed(request)
    }
}