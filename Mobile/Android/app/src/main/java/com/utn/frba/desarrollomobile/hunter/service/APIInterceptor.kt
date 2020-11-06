package com.utn.frba.desarrollomobile.hunter.service

import okhttp3.Interceptor
import okhttp3.Response

class APIInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("Authorization", "Bearer " + APIAdapter.Token)
                .build()
        )
    }
}