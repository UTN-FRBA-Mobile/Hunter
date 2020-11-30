package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.setToolbarTitle
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import com.utn.frba.desarrollomobile.hunter.utils.PermissionHandler
import com.utn.frba.desarrollomobile.hunter.viewmodel.CreateGameViewModel
import kotlinx.android.synthetic.main.fragment_create_game_step_add_image.next_button
import kotlinx.android.synthetic.main.fragment_create_game_step_add_location.*

class CreateGameFragmentStepAddLocation : Fragment(R.layout.fragment_create_game_step_add_location),
    OnMapReadyCallback {

    private val GPS_CODE = 1

    private val GPS_FINE_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    private val GPS_COARSE_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val GPS_PROVIDER = LocationManager.GPS_PROVIDER
    private val UPDATE_TIME = 2500L
    private val UPDATE_DISTANCE = 3f

    private lateinit var gameViewModel: CreateGameViewModel

    private var googleMap: GoogleMap? = null
    private lateinit var locationManager: LocationManager

    private var actualLocation: Location? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameViewModel = ViewModelProvider(requireActivity()).get(CreateGameViewModel::class.java)

        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        updateUI(actualLocation)

        next_button.setOnClickListener {
            showFragment(CreateGameFragmentStepAddImage(), true)
        }

        gps_button.setOnClickListener {
            showGPSRequest()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        map.onCreate(savedInstanceState)
        map.getMapAsync(this)

    }

    private fun updateUI(location: Location?) {
        if (location != null) {
            latitudePreview.visibility = VISIBLE
            longitudePreview.visibility = VISIBLE
            loadingGPS.visibility = GONE
            latitudePreview.text = location.latitude.toString()
            longitudePreview.text = location.longitude.toString()
            drawPinForLocation(location)
            centerMapForLocation(location, animate = true)
            next_button.isEnabled = true
        } else {
            latitudePreview.visibility = GONE
            longitudePreview.visibility = GONE
            loadingGPS.visibility = VISIBLE
            next_button.isEnabled = false
        }
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

            actualLocation = locationManager.getLastKnownLocation(GPS_PROVIDER)
            gameViewModel.setLocation(actualLocation)
            updateUI(actualLocation)

        } else {
            PermissionHandler.requestPermissions(
                this,
                arrayOf(GPS_FINE_PERMISSION, GPS_COARSE_PERMISSION),
                GPS_CODE
            )
        }
    }

    private fun showGPSRequest() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GPS_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PermissionHandler.arePermissionsGranted(
                    requireContext(),
                    arrayOf(GPS_COARSE_PERMISSION, GPS_FINE_PERMISSION)
                )

                actualLocation = locationManager.getLastKnownLocation(GPS_PROVIDER)
                gameViewModel.setLocation(actualLocation)

                updateUI(actualLocation)

                locationManager.requestLocationUpdates(
                    GPS_PROVIDER,
                    UPDATE_TIME,
                    UPDATE_DISTANCE,
                    locationListener
                )
            }

            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(context, "GPS DENEGADO", Toast.LENGTH_SHORT).show()
                showFragment(ChooseGameFragment(), false, true)
            }
        }
    }

    private var locationListener =
        object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                if (location !== null) {
                    updateUI(location)
                    gameViewModel.setLocation(location)
                    Log.d(
                        "GPS",
                        "Location Update: ${location.latitude.toString()} - ${location.longitude.toString()}"
                    )
                }
            }

            override fun onProviderDisabled(provider: String?) {
                gameViewModel.setLocation(null)
                gps_button.visibility = View.VISIBLE
                locationPreview.visibility = View.GONE
                Log.d("GPS", "onProviderDisabled")
            }

            override fun onProviderEnabled(provider: String?) {
                gps_button.visibility = View.GONE
                locationPreview.visibility = View.VISIBLE
                Log.d("GPS", "onProviderENabled")

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                Log.d("GPS", "onStatusChanged")
            }
        }

    override fun onMapReady(map: GoogleMap?) {
        Log.d("GPS", map?.toString() ?: "No hay")
        map?.let {
            googleMap = it
            it.uiSettings.setAllGesturesEnabled(false)
            checkGPSPermissions()
        }
    }

    override fun onStop() {
        locationManager.removeUpdates(locationListener)
        map.onStop()
        super.onStop()
    }

    override fun onStart() {
        map.onStart()
        super.onStart()
    }

    override fun onResume() {
        map.onResume()
        setToolbarTitle(getString(R.string.stepLocation))
        googleMap?.let {
            checkGPSPermissions()
        }
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(GPS_PROVIDER)) {
            gps_button.visibility = VISIBLE
            locationPreview.visibility = GONE
        } else {
            gps_button.visibility = GONE
            locationPreview.visibility = VISIBLE
        }
        updateUI(actualLocation)
        super.onResume()
    }

    private fun centerMapForLocation(
        location: Location,
        zoom: Float? = null,
        animate: Boolean
    ) {
        val locationAsLatLng = LatLng(location.latitude, location.longitude);
        centerMapForLatLng(locationAsLatLng, zoom, animate);
    }

    private fun centerMapForLatLng(location: LatLng, zoom: Float? = null, animate: Boolean) {
        val cameraUpdate: CameraUpdate = if (zoom == null) {
            CameraUpdateFactory.newLatLngZoom(location, 15f)
        } else {
            CameraUpdateFactory.newLatLngZoom(location, zoom)
        }
        if (animate) {
            googleMap?.animateCamera(cameraUpdate)
        } else {
            googleMap?.moveCamera(cameraUpdate)
        }

    }

    private fun drawPinForLocation(location: Location) {
        googleMap?.clear()
        googleMap?.addMarker(
            MarkerOptions()
                .position(LatLng(location.latitude, location.longitude))
                .title("Ubicacion del Tesoro")
        )
    }
}
