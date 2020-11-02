package com.utn.frba.desarrollomobile.hunter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.utn.frba.desarrollomobile.hunter.repository.GameRepository
import com.utn.frba.desarrollomobile.hunter.service.models.Game
import com.utn.frba.desarrollomobile.hunter.service.models.Resource

class GameViewModel : ViewModel() {

    private val gameLiveData: MutableLiveData<Resource<Game>> = MutableLiveData()
    private val repository: GameRepository = GameRepository

    fun getGame(gameId: Int): MutableLiveData<Resource<Game>> {

        repository.getGame(gameId, gameLiveData)

        return gameLiveData
    }

    //TODO for debugging
//    Location(GPS_PROVIDER).apply {
//        longitude = -58.514911; latitude = -34.641733
//    })
}