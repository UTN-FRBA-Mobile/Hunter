package com.utn.frba.desarrollomobile.hunter.viewmodel

import android.graphics.Bitmap
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.utn.frba.desarrollomobile.hunter.service.models.Game

class CreateGameViewModel : ViewModel() {

    private val imageLiveData: MutableLiveData<Bitmap> = MutableLiveData()
    private val clueLiveData: MutableLiveData<String> = MutableLiveData()
    private val locationLiveData: MutableLiveData<Location> = MutableLiveData()
    private val durationLiveData: MutableLiveData<Int> = MutableLiveData()

    private val gameCreatedLiveData: MutableLiveData<Game> = MutableLiveData()

    fun setImage(img: Bitmap?) {
        imageLiveData.postValue(img)
    }

    fun getId(): Int? {
        return gameCreatedLiveData.value?.id
    }

    fun getImage(): MutableLiveData<Bitmap> {
        return imageLiveData
    }

    fun setClue(clue: String?) {
        clueLiveData.postValue(clue)
    }

    fun getClue(): MutableLiveData<String> {
        return clueLiveData
    }

    fun setLocation(location: Location?) {
        locationLiveData.postValue(location)
    }

    fun getLocation(): MutableLiveData<Location> {
        return locationLiveData
    }

    fun setDuration(value: Int?) {
        durationLiveData.postValue(value)
    }

    fun getDuration(): MutableLiveData<Int> {
        return durationLiveData
    }

    fun setGameCreated(game: Game) {
        gameCreatedLiveData.postValue(game)
    }

    fun getGameCreated(): MutableLiveData<Game> {
        return gameCreatedLiveData
    }
}