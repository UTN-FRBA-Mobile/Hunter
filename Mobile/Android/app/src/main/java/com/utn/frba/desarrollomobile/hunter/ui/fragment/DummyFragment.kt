package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.*
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.utils.PermissionHandler
import kotlinx.android.synthetic.main.fragment_dummy.*
import java.lang.Double.valueOf


class DummyFragment : Fragment(R.layout.fragment_dummy), SensorEventListener {

    private lateinit var mSensorManager: SensorManager
    private lateinit var locationManager: LocationManager
    private var actualLocation: Location? = null
    private lateinit var target: Location
    private lateinit var geoField: GeomagneticField
    private lateinit var locationListener: LocationListener

    //PERMISSION
    private val GPS_CODE = 1
    private val GPS_FINE_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    private val GPS_COARSE_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val GPS_PROVIDER = LocationManager.GPS_PROVIDER

    // compass arrow degree direction
    private var currentDegree = 0f

    private val UPDATE_TIME = 5000L
    private val UPDATE_DISTANCE = 10f

    @SuppressLint("MissingPermission")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //TODO check if GPS is ON and alert user otherwise

        mSensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        target = Location(GPS_PROVIDER)

        setTargetLocation()

        submitLocationBtn.setOnClickListener { setTargetLocation() }

        locationListener = getLocationListener()

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
        } else {
            PermissionHandler.requestPermissions(
                this,
                arrayOf(GPS_FINE_PERMISSION, GPS_COARSE_PERMISSION),
                GPS_CODE
            )
        }
    }

    private fun setTargetLocation() {
        target.latitude = latitudeEditText.text.toString().toDouble()
        target.longitude = longitudeEditText.text.toString().toDouble()
    }

    private fun getLocationListener() =
        object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                actualLocation = location

                actualLocation?.let {
                    with(it) {

                        geoField = GeomagneticField(
                            valueOf(latitude).toFloat(),
                            valueOf(longitude).toFloat(),
                            valueOf(altitude).toFloat(),
                            System.currentTimeMillis()
                        )
                    }
                }
            }

            override fun onProviderDisabled(provider: String?) {
                //TODO alert user to turn GPS on
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

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // not in use
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

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GPS_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            PermissionHandler.arePermissionsGranted(
                context!!,
                arrayOf(GPS_COARSE_PERMISSION, GPS_FINE_PERMISSION)
            )
            locationManager.requestLocationUpdates(
                GPS_PROVIDER,
                UPDATE_TIME,
                UPDATE_DISTANCE,
                locationListener
            )
        }
    }
}
