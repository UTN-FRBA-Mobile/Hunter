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
                val body = response.body()
                body?.let {
                    gameLiveData.postValue(Resource.success(it.apply { photo = photo?.trim() }))
                } ?: run { gameLiveData.postValue(Resource.error("error", null)) }
            }
        }

        APIAdapter.getAPI().getGame(gameId).enqueue(gameCallback)
    }

}
