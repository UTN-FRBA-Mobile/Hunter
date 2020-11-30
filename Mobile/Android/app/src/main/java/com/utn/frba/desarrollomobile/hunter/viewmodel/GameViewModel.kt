package com.utn.frba.desarrollomobile.hunter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.utn.frba.desarrollomobile.hunter.repository.GameRepository
import com.utn.frba.desarrollomobile.hunter.service.models.Game
import com.utn.frba.desarrollomobile.hunter.service.models.Resource

class GameViewModel : ViewModel() {

    private val gameLiveData: MutableLiveData<Resource<Game>> = MutableLiveData()
    private val repository: GameRepository = GameRepository
    private lateinit var game: Game

    fun getGame(gameId: Int): MutableLiveData<Resource<Game>> {

        val gameLoaded =
            gameLiveData.value?.let { it.isSuccessful && it.data?.id == gameId } ?: run { false }

        if (!gameLoaded) {
            repository.getGame(gameId, gameLiveData)
        }

        return gameLiveData
    }

    fun setGame(game: Game) {
        this.game = game
    }

    fun getDetailGame(): Game {
        return game
    }
}