package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.removeFragment
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import com.utn.frba.desarrollomobile.hunter.service.APIAdapter
import com.utn.frba.desarrollomobile.hunter.service.models.User
import com.utn.frba.desarrollomobile.hunter.ui.activity.MainActivity
import com.utn.frba.desarrollomobile.hunter.utils.PermissionHandler
import com.utn.frba.desarrollomobile.hunter.viewmodel.CreateGameViewModel
import kotlinx.android.synthetic.main.fragment_create_game_step_add_image.next_button
import kotlinx.android.synthetic.main.fragment_create_game_step_add_location.*
import kotlinx.android.synthetic.main.fragment_create_game_step_add_location.latitudePreview
import kotlinx.android.synthetic.main.fragment_create_game_step_add_location.longitudePreview
import kotlinx.android.synthetic.main.fragment_create_game_step_add_location.map
import kotlinx.android.synthetic.main.fragment_create_game_step_review.*
import kotlinx.android.synthetic.main.fragment_map.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateGameFragmentStepAddLocation : Fragment(R.layout.fragment_create_game_step_add_location) {

    private val GPS_CODE = 1

    private val GPS_FINE_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    private val GPS_COARSE_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val GPS_PROVIDER = LocationManager.GPS_PROVIDER
    private val UPDATE_TIME = 2500L
    private val UPDATE_DISTANCE = 3f

    private lateinit var gameViewModel: CreateGameViewModel

    //private lateinit var googleMap: GoogleMap
    private lateinit var locationManager: LocationManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameViewModel = ViewModelProvider(requireActivity()).get(CreateGameViewModel::class.java)

        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        updateUI(null)
        checkGPSPermissions()

        gameViewModel.getLocation().observe(viewLifecycleOwner, Observer { location ->
            updateUI(location)
        })

        next_button.setOnClickListener {
            locationManager.removeUpdates(locationListener)
            showFragment(CreateGameFragmentStepAddImage(), true)
        }

        gps_button.setOnClickListener {
            showGPSRequest()
        }
    }

    private fun updateUI(location: Location?) {
        if (location != null) {
            latitudePreview.visibility = View.VISIBLE
            longitudePreview.visibility = View.VISIBLE
            loadingGPS.visibility = View.GONE
            latitudePreview.text = location.latitude.toString()
            longitudePreview.text = location.longitude.toString()
            next_button.isEnabled = true
        } else {
            latitudePreview.visibility = View.GONE
            longitudePreview.visibility = View.GONE
            loadingGPS.visibility = View.VISIBLE
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
            locationListener.onProviderEnabled(GPS_PROVIDER)
        } else {
            locationListener.onProviderDisabled(null)
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
                    Log.d("GPS", "Location Update: ${location.latitude.toString()} - ${location.longitude.toString()}")
                }
            }

            override fun onProviderDisabled(provider: String?) {
                updateUI(null)
                gameViewModel.setLocation(null)
                gps_button.visibility = View.VISIBLE
                locationPreview.visibility = View.GONE
                Toast.makeText(context, "GPS DISABLE", Toast.LENGTH_SHORT).show()
                Log.d("GPS", "onProviderDisabled")
            }

            override fun onProviderEnabled(provider: String?) {
                Toast.makeText(context, "GPS ENABLED", Toast.LENGTH_SHORT).show()
                gps_button.visibility = View.GONE
                locationPreview.visibility = View.VISIBLE
                Log.d("GPS", "onProviderENabled")

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                Log.d("GPS", "onStatusChanged")
            }
        }

//    override fun onMapReady(map: GoogleMap?) {
//        Log.d("DEBUG LOCATION", map?.toString() ?: "No hay")
//        map ?: return
//        googleMap = map
//        map.addMarker(
//            MarkerOptions()
//                .position(LatLng(-34.6036844, -58.3815591))
//                .title("Centro")
//        )
//
//        centerMapForLatLng(LatLng(-34.6036844, -58.3815591))
//    }
//
//    private fun centerMapForLatLng(location: LatLng) {
//        var cameraUpdate = CameraUpdateFactory.newLatLng(location)
//        googleMap.animateCamera(cameraUpdate)
//
//    }


}
