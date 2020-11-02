package com.utn.frba.desarrollomobile.hunter.repository

import androidx.lifecycle.MutableLiveData
import com.utn.frba.desarrollomobile.hunter.service.APIAdapter
import com.utn.frba.desarrollomobile.hunter.service.models.Game
import com.utn.frba.desarrollomobile.hunter.service.models.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object GameRepository {

    fun getGame(gameId: Int, gameLiveData: MutableLiveData<Resource<Game>>) {

        gameLiveData.postValue(Resource.loading())

        val gameCallback = object : Callback<Game> {

            override fun onFailure(call: Call<Game>, t: Throwable) {
                gameLiveData.postValue(Resource.error("error", null))
            }

            override fun onResponse(call: Call<Game>, response: Response<Game>) {
//                val body = response.body()
//                body?.let {
//                    gameLiveData.postValue(Resource.success(it))
//                } ?: run { gameLiveData.postValue(Resource.error("error", null)) }

                //TODO for debugging
                gameLiveData.postValue(Resource.success(Game().apply {
                    longitude = (-58.514911).toFloat(); latitude = (-34.641733).toFloat()
                    photo =
                        "https://d500.epimg.net/cincodias/imagenes/2020/06/19/lifestyle/1592554496_568289_1592555393_sumario_normal.jpg"
                    clues = arrayOf("Fijate cerca del contenedor de basura ;)")
                }))
            }
        }

        APIAdapter.getAPI().getGame(gameId).enqueue(gameCallback)
    }

}
