package com.utn.frba.desarrollomobile.hunter.service

import com.utn.frba.desarrollomobile.hunter.service.models.Game
import com.utn.frba.desarrollomobile.hunter.service.models.GameResponse
import com.utn.frba.desarrollomobile.hunter.service.models.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @GET("/api/Game/Get?")
    fun getGame(
        @Header("Authorization") token: String,
        @Query("id") game_id: Int
    ): Call<GameResponse>

    @GET("/api/Game/GetMyGames")
    fun getMyCreatedGames(
        @Header("Authorization") token: String
    ): Call<ArrayList<GameResponse>>

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
    ): Call<GameResponse>

    @POST("/api/Game/Win")
    @FormUrlEncoded
    fun winGame(
        @Header("Authorization") token: String,
        @Field("game_id") game_id: Int,
        @Field("win_code") win_code: String
    ): Call<GameResponse>

    @GET("/api/User/Get")
    fun getUser(@Header("Authorization") token: String): Call<UserResponse>

    @GET("/api/User/History")
    fun getMyHistoryGames(@Header("Authorization") token: String
    ): Call<ArrayList<GameResponse>>

    @POST("/api/User/Post")
    @FormUrlEncoded
    fun setUser(
        @Header("Authorization") token: String,
        @Field("alias") alias: String,
        @Field("mail") mail: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String
    ): Call<UserResponse>
}