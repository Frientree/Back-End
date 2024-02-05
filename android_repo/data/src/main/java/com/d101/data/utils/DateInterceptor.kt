package com.d101.data.utils


import okhttp3.Interceptor
import okhttp3.Response
import java.time.LocalDate
import javax.inject.Inject

class DateInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Date", LocalDate.now().toString())
            .build()

        return chain.proceed(request)
    }
}
