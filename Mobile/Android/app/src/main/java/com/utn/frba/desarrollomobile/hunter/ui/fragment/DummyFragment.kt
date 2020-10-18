package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.content.Context
import android.hardware.*
import android.location.Location
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import kotlinx.android.synthetic.main.fragment_dummy.*
import java.lang.Double


class DummyFragment : BaseLocationFragment(R.layout.fragment_dummy), SensorEventListener {

    // compass arrow degree direction
    private var currentDegree = 0f

    private fun setTargetLocation() {
        target.latitude = latitudeEditText.text.toString().toDouble()
        target.longitude = longitudeEditText.text.toString().toDouble()
    }

    override fun init(savedInstanceState: Bundle?) {
        mSensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
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
        showFragment(MapFragment(), true)
    }

    override fun onResume() {
        super.onResume()
        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(
            this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onPause() {
        super.onPause()
        // to stop the listener and save battery
        mSensorManager.unregisterListener(this)
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
            if (distance > TARGET_RADIUS) {
                showFragment(MapFragment(), true)
            } else if (distance <= 15) {
                Toast.makeText(context, "ESTAS MUY CERCA! PEDI PISTA!", LENGTH_LONG).show()
            }
        }
    }
}
