package com.example.ict2105project.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import java.util.*

class LocationService : Service() {
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null


    /**
     * no need to bind to because this is a started service
     */
    override fun onBind(p0: Intent?): IBinder? {
        return null;
    }

    /**
     * sets up the location callback to get the location
     * after getting the location (latitude & longitude), it will be converted
     * to something readable, like city and postal codes
     * following after, it will be broadcasted to the fragment
     */
    override fun onCreate() {
        super.onCreate()
        Log.d("LOCATION SERVICE UP", "LOCATION SERVICE UP")
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            //get lattitude and longtitude
            override fun onLocationResult(location: LocationResult?) {
                super.onLocationResult(location)
                Log.d(
                    "LATITUDE / LONGTITUDE", location!!.lastLocation.latitude.toString()
                            + " / " + location.lastLocation.longitude.toString()
                )

                //getting location information
                //https://stackoverflow.com/questions/59095837/convert-from-latlang-to-address-using-geocoding-not-working-android-kotlin
                val geocoder = Geocoder(applicationContext, Locale.getDefault())
                val addr: MutableList<Address>? = geocoder.getFromLocation(
                    location.lastLocation.latitude,
                    location.lastLocation.longitude,
                    1
                )
                if (addr != null && addr.isNotEmpty()) {
                    Log.d("AREA:", addr[0].getAddressLine(0).toString())

                    //do a broadcast so that fragment can receive the message
                    currentLocation = addr[0].getAddressLine(0).toString()

                    Log.d("LOCATION DATA BC:", currentLocation)
                    sendBroadcast(
                        Intent("CURRENTLOCATION").putExtra(
                            "location_key",
                            currentLocation
                        ).setAction("com.example.ict2105project")
                    )
                    //stop the location callbacks
                    //https://stackoverflow.com/questions/48787940/fusedlocationclient-doesnt-stop-searching-for-gps-after-request
                    fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
                    fusedLocationProviderClient = null
                    locationCallback = null
                }
            }
        }
    }

    /**
     * starts the service
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        requestLocation()
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * Destroys the service
     */
    override fun onDestroy() {
        super.onDestroy()
        stopSelf() //needs to be added to stop the service
        Log.d("LOCATION SERVICE DOWN", "LOCATION SERVICE STOPPED")
    }

    /**
     * request for the location thorugh looper every 5s
     * this however will be killed after Home Fragment has received 1 broadcast
     */
    @SuppressLint("MissingPermission")
    private fun requestLocation(){
        //https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest
        val locationRequest: LocationRequest = LocationRequest()
        locationRequest.interval = 5000    //every 5s
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        fusedLocationProviderClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    //in the event if any fragments require some access to the current location, this could be used
    companion object {
        var currentLocation = "No Data"
    }
}