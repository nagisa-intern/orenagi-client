package com.nagisa.orenagi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.security.AccessController.getContext
import android.widget.TextView
import android.location.*
import android.location.LocationManager
import android.location.LocationListener


class MainActivity : AppCompatActivity() {
    private var locationManager : LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create persistent LocationManager reference
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?;

        try {
            // Request location updates
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener);
        } catch(ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available");
        }

    }

    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val textView1 = findViewById<TextView>(R.id.text_view1)

            textView1.text = "" + location.longitude + ":" + location.latitude
            Log.d("TAG", "============================")
            Log.d("TAG", location.latitude.toString())
            Log.d("TAG", "============================")

            if (!Geocoder.isPresent()) return
            val coder = Geocoder(getContext())
            val addresses = coder.getFromLocation(
                    location.getLatitude(), location.getLongitude(), 1)
            Log.d("location", addresses[0].toString())
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }
}
