package com.utn.frba.desarrollomobile.hunter.viewmodel

import android.location.Location
import android.location.LocationManager.GPS_PROVIDER
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel() {

    private val locationLiveData: MutableLiveData<Location> = MutableLiveData()

    fun getTargetLocation(): MutableLiveData<Location> {
        locationLiveData.postValue(Location(GPS_PROVIDER).apply {
            longitude = -58.514911; latitude = -34.641733
        })

        return locationLiveData
    }
}