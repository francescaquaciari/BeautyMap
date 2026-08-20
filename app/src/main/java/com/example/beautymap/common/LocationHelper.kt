package com.example.beautymap.common

import android.Manifest
import android.content.Context
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationHelper (context: Context){

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)    //serve per ottenere la posizione corrente

    private val locationRequest = LocationRequest.Builder(0)                         //serve per specificare la frequenza di aggiornamento della posizione
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)                                             //precisazione della posizione
        .build()                                                                                  //costruzione della richiesta

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun start (callback: LocationCallback)  {                                                     //serve per iniziare l'aggiornamento della posizione
        fusedLocationClient.requestLocationUpdates(locationRequest, callback, Looper.getMainLooper())   //richiesta di aggiornamento della posizione
    }

    fun stop (callback: LocationCallback)  {                                                     //serve per fermare l'aggiornamento della posizione
        fusedLocationClient.removeLocationUpdates(callback)
    }
    }