package com.utn.frba.desarrollomobile.hunter.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIAdapter {
    val ROOT_URL = "https://utn-mobile-hunter.herokuapp.com"
    //val ROOT_URL = "http://192.168.0.153:5000"
    var Token = ""
    var retrofit: Retrofit

    init {
        val httpClient = OkHttpClient.Builder();
        httpClient.addInterceptor(APIInterceptor())
        retrofit = Retrofit.Builder()
            .baseUrl(ROOT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

    fun getAPI(): APIService {
        return retrofit.create(APIService::class.java)
    }
}