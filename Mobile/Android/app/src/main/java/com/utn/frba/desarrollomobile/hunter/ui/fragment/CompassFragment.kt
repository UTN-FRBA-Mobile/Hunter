package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.content.Context
import android.hardware.*
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AlertDialog
import com.squareup.picasso.Picasso
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.setToolbarTitle
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import kotlinx.android.synthetic.main.dialog_clue_layout.view.*
import kotlinx.android.synthetic.main.fragment_compass.*
import java.lang.Double


class CompassFragment : BaseLocationFragment(R.layout.fragment_compass), SensorEventListener {

    // compass arrow degree direction
    private var currentDegree = 0f
    private val CLUE_RADIUS = 15

    override fun init() {
        mSensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        showClueButton.setOnClickListener { showClue() }
    }

    override fun onSensorChanged(event: SensorEvent) { // get the angle around the z-axis rotated
        actualLocation?.let {
            var degree = Math.round(event.values[0]).toFloat()
            val declination = geoField.getDeclination()
            degree += declination
            val bearing: Float = it.bearingTo(target)
            degree = (bearing - degree) * -1
            degree = normalizeDegree(degree)

            // create a rotation animation (reverse turn degree degrees)
            val ra = RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            // how long the animation will take place
            ra.duration = 210
            // set the animation after the end of the reservation status
            ra.fillAfter = true
            // Start the animation
            compassView.startAnimation(ra)
            currentDegree = -degree
        }
    }

    private fun normalizeDegree(value: Float): Float {
        return if (value < 0) {
            value + 360
        } else {
            value
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // not in use
    }


    private fun goToMapFragment() {
        showFragment(MapFragment().apply {
            arguments = Bundle().apply { putInt(GAME_ID, game.id.toString().toInt()) }
        }, true)
    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle(getString(R.string.compass_title))
        // for the system's orientation sensor registered listeners
        mSensorManager?.registerListener(
            this, mSensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onPause() {
        super.onPause()
        // to stop the listener and save battery
        mSensorManager?.unregisterListener(this)
    }

    override fun onLocationUpdated(actualLocation: Location) {
        with(actualLocation) {
            geoField = GeomagneticField(
                Double.valueOf(latitude).toFloat(),
                Double.valueOf(longitude).toFloat(),
                Double.valueOf(altitude).toFloat(),
                System.currentTimeMillis()
            )
            val distance = target.distanceTo(actualLocation)
            when {
                distance > TARGET_RADIUS -> {

                    Log.d("HUNTER", "distance > TARGET_RADIUS")
                    goToMapFragment()
                }
                distance <= CLUE_RADIUS -> {
                    Log.d("HUNTER", "distance <= TARGET_RADIUS")

                    showClueButton.visibility = VISIBLE
                }
                else -> {
                    Log.d("HUNTER", "else")

                    showClueButton.visibility = GONE
                }
            }
        }
    }

    private fun showClue() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_clue_layout, null)

        val alert: AlertDialog?
        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
        alert = builder.create()
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.clueText.text = game.clues.first()

        dialogView.clueCloseBtn.setOnClickListener { alert.dismiss() }
        dialogView.clueImage.layoutParams.width =
            (resources.displayMetrics.widthPixels * 0.8).toInt()

        if (!game.photo.isNullOrEmpty()) {
            Picasso.get().load(game.photo).into(dialogView.clueImage)
            dialogView.clueImage.visibility = VISIBLE
        }

        alert.show()
    }
}
