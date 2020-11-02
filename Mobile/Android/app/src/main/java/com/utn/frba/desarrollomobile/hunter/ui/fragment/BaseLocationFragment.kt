package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.GeomagneticField
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.service.models.Game
import com.utn.frba.desarrollomobile.hunter.ui.activity.MainActivity
import com.utn.frba.desarrollomobile.hunter.utils.PermissionHandler
import com.utn.frba.desarrollomobile.hunter.viewmodel.GameViewModel

abstract class BaseLocationFragment(layoutId: Int) : Fragment(layoutId) {

    protected var mSensorManager: SensorManager? = null
    private lateinit var locationManager: LocationManager
    protected lateinit var geoField: GeomagneticField
    private lateinit var locationListener: LocationListener

    protected var actualLocation: Location? = null
    protected lateinit var target: Location

    //PERMISSION
    private val GPS_CODE = 1
    private val GPS_FINE_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    private val GPS_COARSE_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val GPS_PROVIDER = LocationManager.GPS_PROVIDER

    private val UPDATE_TIME = 5000L
    private val UPDATE_DISTANCE = 10f

    private lateinit var gameViewModel: GameViewModel
    protected var gameID: Int = 0

    protected lateinit var game: Game

    protected abstract fun init()
    protected abstract fun onLocationUpdated(actualLocation: Location)

    companion object {

        const val TARGET_RADIUS = 200000.0
        const val GAME_ID = "gameID"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        gameViewModel = ViewModelProvider(requireActivity()).get(GameViewModel::class.java)

        checkGPSIsON()

        gameID =
            arguments?.getInt(GAME_ID) ?: -1 //default gameID -1 will return error from service

        gameViewModel.getGame(gameID)
            .observe(viewLifecycleOwner, Observer { gameResponse ->
                when {
                    gameResponse.isLoading -> showLoading()
                    gameResponse.isSuccessful -> gameResponse.data?.let { onGameFetched(it) }
                        ?: run { showError() }
                    gameResponse.isError -> showError()
                }
            })

        locationListener = getLocationListener()

    }

    private fun getLocationListener() =
        object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                actualLocation = location

                actualLocation?.let {
                    onLocationUpdated(it)
                }
            }

            override fun onProviderDisabled(provider: String?) {
                showGPSDialog()
                Log.d("GPS", "onProviderDisabled")
            }

            override fun onProviderEnabled(provider: String?) {
                Toast.makeText(context, "GPS ENABLED", Toast.LENGTH_SHORT).show()
                Log.d("GPS", "onProviderENabled")

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                Log.d("GPS", "onStatusChanged")
            }
        }

    private fun onGameFetched(game: Game) {
        this.game = game
        target = Location(GPS_PROVIDER).apply {
            longitude = game.longitude.toDouble()
            latitude = game.latitude.toDouble()
        }

        checkGPSPermissions()

    }

    @SuppressLint("MissingPermission")
    private fun checkGPSPermissions() {
        if (PermissionHandler.arePermissionsGranted(
                requireContext(),
                arrayOf(GPS_FINE_PERMISSION, GPS_COARSE_PERMISSION)
            )
        ) {
            locationManager.requestLocationUpdates(
                GPS_PROVIDER,
                UPDATE_TIME,
                UPDATE_DISTANCE,
                locationListener
            )
            init()

            hideLoading()
        } else {
            PermissionHandler.requestPermissions(
                this,
                arrayOf(GPS_FINE_PERMISSION, GPS_COARSE_PERMISSION),
                GPS_CODE
            )
        }
    }

    private fun showError() {
        hideLoading()
        //TODO avisar al user
        activity?.onBackPressed()
        Toast.makeText(context, "Ocurrió un error al buscar el juego $gameID", LENGTH_LONG).show()
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GPS_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            PermissionHandler.arePermissionsGranted(
                requireContext(),
                arrayOf(GPS_COARSE_PERMISSION, GPS_FINE_PERMISSION)
            )
            locationManager.requestLocationUpdates(
                GPS_PROVIDER,
                UPDATE_TIME,
                UPDATE_DISTANCE,
                locationListener
            )
            init()

            hideLoading()
        } else {
            //TODO avisar que se necesita el permiso
            showError()
        }
    }

    private fun checkGPSIsON() {
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(GPS_PROVIDER)) {
            showGPSDialog()
        }
    }

    private fun showGPSDialog() {
        val alert: AlertDialog?
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.enable_gps_text))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.enable_gps_positive_text)) { _, _ ->
                startActivity(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                )
            }
            .setNegativeButton(getString(R.string.enable_gps_negative_text)) { dialog, _ -> dialog.dismiss() }
        alert = builder.create();
        alert.show();
    }

    protected fun showLoading() {
        (activity as MainActivity).showLoading("Ingresando...")
    }

    protected fun hideLoading() {
        (activity as MainActivity).hideLoading()
    }

    override fun onStop() {
        // stop location updating
        locationManager.removeUpdates(locationListener)
        super.onStop()
    }
}