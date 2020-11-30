package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.setToolbarTitle
import com.utn.frba.desarrollomobile.hunter.utils.LoginHandler
import com.utn.frba.desarrollomobile.hunter.viewmodel.GameViewModel
import kotlinx.android.synthetic.main.detail_history_fragment.*

class HistoryDetailFragment : Fragment(R.layout.detail_history_fragment) {

    private lateinit var gameViewModel: GameViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        game_detail_map.onCreate(savedInstanceState)
        game_detail_map.getMapAsync { onMapReady(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameViewModel = ViewModelProvider(requireActivity()).get(GameViewModel::class.java)
    }

    private fun onMapReady(googleMap: GoogleMap) {
        val game = gameViewModel.getDetailGame()

        game_detail_date.text = game.endDatetime?.substring(0, 10)

        if (game.winId == LoginHandler.USERID) {
            game_detail_winner.text = "GANÉ!!!"
        } else {
            game_detail_winner.text = "Perdí..."
        }

        if (!game.photo.isNullOrEmpty()) {
            try {
                Picasso.get().load(game.photo).into(game_detail_photo)
            } catch (e: Exception) {
            }
        }

        googleMap.uiSettings.isRotateGesturesEnabled = false

        val latitude = game.latitude.toDouble()
        val longitude = game.longitude.toDouble()

        drawCircle(latitude, longitude, googleMap)
        drawPinForLocation(googleMap, latitude, longitude)
        centerMapForLocation(latitude, longitude, animate = false, googleMap = googleMap)
    }

    private fun drawCircle(latitude: Double, longitude: Double, googleMap: GoogleMap) {
        val circle: Circle = googleMap.addCircle(
            CircleOptions()
                .center(LatLng(latitude, longitude))
                .radius(BaseLocationFragment.TARGET_RADIUS)
                .strokeColor(Color.RED)
                .fillColor(ContextCompat.getColor(requireContext(), R.color.circle_fill_color))
        )
    }

    private fun centerMapForLocation(
        latitude: Double,
        longitude: Double,
        zoom: Float? = null,
        animate: Boolean,
        googleMap: GoogleMap
    ) {
        val locationAsLatLng = LatLng(latitude, longitude);
        centerMapForLatLng(locationAsLatLng, zoom, animate, googleMap);
    }

    private fun centerMapForLatLng(
        location: LatLng,
        zoom: Float? = null,
        animate: Boolean,
        googleMap: GoogleMap
    ) {
        val cameraUpdate: CameraUpdate = if (zoom == null) {
            CameraUpdateFactory.newLatLngZoom(location, 15f)
        } else {
            CameraUpdateFactory.newLatLngZoom(location, zoom)
        }
        if (animate) {
            googleMap.animateCamera(cameraUpdate)
        } else {
            googleMap.moveCamera(cameraUpdate)
        }
    }

    private fun drawPinForLocation(googleMap: GoogleMap, latitude: Double, longitude: Double) {
        googleMap.clear()
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude))
                .title("Ubicacion del Tesoro")
        )
    }


    override fun onResume() {
        game_detail_map.onResume()
        setToolbarTitle(getString(R.string.historyGame))
        super.onResume()
    }

    override fun onPause() {
        game_detail_map.onPause()
        super.onPause()
    }

    override fun onLowMemory() {
        game_detail_map.onLowMemory()
        super.onLowMemory()
    }

    override fun onStart() {
        game_detail_map.onStart()
        super.onStart()
    }

    override fun onStop() {
        game_detail_map.onStop()
        super.onStop()
    }

}
