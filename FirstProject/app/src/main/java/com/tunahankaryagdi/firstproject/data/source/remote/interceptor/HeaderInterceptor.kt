package com.tunahankaryagdi.firstproject.data.source.remote.interceptor

import com.tunahankaryagdi.firstproject.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
        return chain.proceed(request.build())
    }
}
