package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.removeFragment
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : BaseLocationFragment(R.layout.fragment_map) {

    /**
     * Unirse a un juego -> ingresa codigo -> GET /game -> Loading -> MapFragment
     *
     * Viewmodel location
     * Zoom del mapa -> que se vea el circulo y la ubicacion actual
     * MapFragment -> onLocationChanged calcula target - actual <= radio entonces remove MapFragment y show CompassFragment y send Push al resto de participantes
     * En CompassFragment -> onLocationChanged calcula target - actual > radio entonces remove CompassFragment y show MapFragment.
     * En CompassFragment -> si target - actual <= 15 mostrar PistaButton
     */

    private lateinit var googleMap: GoogleMap

    override fun init(savedInstanceState: Bundle?) {
        map.onCreate(savedInstanceState)
        map.getMapAsync { onMapReady(it) }
    }

    private fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap
        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isRotateGesturesEnabled = false

        drawCircle(target)
        centerMapForLocation(target, true, false)
    }

    private fun drawCircle(targetLocation: Location) {
        val circle: Circle = googleMap.addCircle(
            CircleOptions()
                .center(LatLng(targetLocation.latitude, targetLocation.longitude))
                .radius(TARGET_RADIUS)
                .strokeColor(Color.RED)
                .fillColor(ContextCompat.getColor(requireContext(), R.color.circle_fill_color))
        )
    }

    private fun centerMapForLocation(
        location: Location,
        useDefaultZoom: Boolean,
        animate: Boolean
    ) {
        val locationAsLatLng = LatLng(location.latitude, location.longitude);
        centerMapForLatLng(locationAsLatLng, useDefaultZoom, animate);
    }

    private fun centerMapForLatLng(location: LatLng, useDefaultZoom: Boolean, animate: Boolean) {
        val cameraUpdate: CameraUpdate = if (useDefaultZoom) {
            CameraUpdateFactory.newLatLngZoom(location, 15f)
        } else {
            CameraUpdateFactory.newLatLng(location)
        }
        if (animate) {
            googleMap.animateCamera(cameraUpdate)
        } else {
            googleMap.moveCamera(cameraUpdate)
        }
    }

    override fun onResume() {
        map.onResume()
        super.onResume()
    }

    override fun onPause() {
        map.onPause()
        super.onPause()
    }

    override fun onLowMemory() {
        map.onLowMemory()
        super.onLowMemory()
    }

    override fun onStart() {
        map.onStart()
        super.onStart()
    }

    override fun onStop() {
        map.onStop()
        super.onStop()
    }

    override fun onLocationUpdated(actualLocation: Location) {
        //target - actual <= radio entonces remove MapFragment
        if (target.distanceTo(actualLocation) <= TARGET_RADIUS) {
            removeFragment()
            showFragment(DummyFragment(), true)
        }
    }
}