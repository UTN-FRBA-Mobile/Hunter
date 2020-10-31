package com.utn.frba.desarrollomobile.hunter.service

import com.utn.frba.desarrollomobile.hunter.service.models.Game
import com.utn.frba.desarrollomobile.hunter.service.models.User
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @GET("/api/Game/Get?")
    fun getGame(
        @Header("Authorization") token: String,
        @Query("id") game_id: Int
    ): Call<Game>

    @GET("/api/Game/GetMyGames")
    fun getMyCreatedGames(
        @Header("Authorization") token: String
    ): Call<ArrayList<Game>>

    @POST("/api/Game/Post")
    @FormUrlEncoded
    fun setGame(
        @Header("Authorization") token: String,
        @Field("duration_mins") duration_mins: Int,
        @Field("latitude") latitude: Float,
        @Field("longitude") longitude: Float,
        @Field("clues") clues: ArrayList<String>,
        @Field("user_ids") user_ids: ArrayList<Int>,
        @Field("photo") photo: String
    ): Call<Game>

    @POST("/api/Game/Win")
    @FormUrlEncoded
    fun winGame(
        @Header("Authorization") token: String,
        @Field("game_id") game_id: Int,
        @Field("win_code") win_code: String
    ): Call<Game>

    @GET("/api/User/Get")
    fun getUser(@Header("Authorization") token: String): Call<User>

    @GET("/api/User/History")
    fun getMyHistoryGames(@Header("Authorization") token: String
    ): Call<ArrayList<Game>>

    @POST("/api/User/Post")
    @FormUrlEncoded
    fun setUser(
        @Header("Authorization") token: String,
        @Field("alias") alias: String,
        @Field("mail") mail: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String
    ): Call<User>
}