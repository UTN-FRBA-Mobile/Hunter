package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.viewmodel.LocationViewModel
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : Fragment(R.layout.fragment_map) {

    private lateinit var googleMap: GoogleMap
    private lateinit var locationViewModel: LocationViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        locationViewModel = ViewModelProvider(requireActivity()).get(LocationViewModel::class.java)

        map.onCreate(savedInstanceState)
        map.getMapAsync { onMapReady(it) }

    }

    private fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap
        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isRotateGesturesEnabled = false

        locationViewModel.getTargetLocation()
            .observe(viewLifecycleOwner, Observer { targetLocation ->
                drawCircle(targetLocation)
                centerMapForLocation(targetLocation, true, false)
            })
    }

    private fun drawCircle(targetLocation: Location) {
        val circle: Circle = googleMap.addCircle(
            CircleOptions()
                .center(LatLng(targetLocation.latitude, targetLocation.longitude))
                .radius(200.0)
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

}