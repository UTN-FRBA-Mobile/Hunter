package com.utn.frba.desarrollomobile.hunter.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class APIAdapter {
    companion object {

        val ROOT_URL = "https://utn-mobile-hunter.herokuapp.com"

        fun createConection(gson: Gson?): APIService? {
            val retrofit = Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            return retrofit.create(APIService::class.java)
        }

        fun createConection(): APIService? {
            val retrofit = Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(APIService::class.java)
        }

        fun createConection(
            returnClasss: Class<*>?,
            deserializador: JsonDeserializer<*>?
        ): APIService? {
            val gson =
                GsonBuilder().registerTypeAdapter(returnClasss, deserializador)
                    .disableHtmlEscaping()
                    .create()
            val retrofit = Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            return retrofit.create(APIService::class.java)
        }
    }
}