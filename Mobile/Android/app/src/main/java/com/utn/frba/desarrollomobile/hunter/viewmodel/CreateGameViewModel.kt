package com.utn.frba.desarrollomobile.hunter.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.utn.frba.desarrollomobile.hunter.service.models.Game

class CreateGameViewModel : ViewModel() {

    private val imageLiveData: MutableLiveData<Bitmap> = MutableLiveData()
    private val clueLiveData: MutableLiveData<String> = MutableLiveData()
    private val playersLiveData: MutableLiveData<MutableList<String>> = MutableLiveData()
    private val gameCreatedLiveData: MutableLiveData<Game> = MutableLiveData()
    private var game: Game? = null

    fun setImage(img: Bitmap) {
        imageLiveData.postValue(img)
    }

    fun getImage(): MutableLiveData<Bitmap> {
        return imageLiveData
    }

    fun setClue(clue: String) {
        clueLiveData.postValue(clue)
    }

    fun getClue(): MutableLiveData<String> {
        return clueLiveData
    }

    fun addPlayer(player: String) {
        val values: MutableList<String> = playersLiveData.value.orEmpty().toMutableList()
        values.add(player)
        playersLiveData.postValue(values)
    }

    fun removePlayer(player: String) {
        val values: MutableList<String> = playersLiveData.value.orEmpty().toMutableList()
        values.remove(player)
        playersLiveData.postValue(values)
    }

    fun getPlayers(): MutableLiveData<MutableList<String>> {
        return playersLiveData
    }

    fun setGameCreated(game: Game) {
        gameCreatedLiveData.postValue(game)
        this.game = game
    }

    fun getGameCreated(): Game? {
        return this.game
        //return gameCreatedLiveData
    }
}