package com.utn.frba.desarrollomobile.hunter.service

import com.utn.frba.desarrollomobile.hunter.service.models.Game
import com.utn.frba.desarrollomobile.hunter.service.models.User
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @GET("/api/Game/Get?")
    fun getGame(
        @Query("id") game_id: Int
    ): Call<Game>

    @GET("/api/Game/GetMyGames")
    fun getMyCreatedGames(): Call<ArrayList<Game>>

    @POST("/api/Game/Post")
    @FormUrlEncoded
    fun setGame(
        @Field("durationMins") durationMins: Int,
        @Field("latitude") latitude: Float,
        @Field("longitude") longitude: Float,
        @Field("clues") clues: Array<String>,
        @Field("userIds") userIds: Array<Int>,
        @Field("photo") photo: String) : Call<Game>

    @POST("/api/Game/Win")
    @FormUrlEncoded
    fun winGame(
        @Field("game_id") game_id: Int,
        @Field("win_code") win_code: String
    ): Call<Game>

    @GET("/api/User/Get")
    fun getUser(): Call<User>

    @GET("/api/User/History")
    fun getMyHistoryGames(): Call<ArrayList<Game>>

    @POST("/api/User/Post")
    fun setUser(@Body user: User): Call<User>

    @GET("/api/User/Find")
    fun findUser(@Body user: User): Call<User>
}