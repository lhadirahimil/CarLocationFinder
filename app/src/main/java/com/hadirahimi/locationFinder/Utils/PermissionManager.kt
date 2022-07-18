package com.hadirahimi.locationFinder.Utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.contentValuesOf
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PermissionManager @Inject constructor(@ApplicationContext val  context : Context)
{
    fun checkPermission() : Boolean
    {
        //check gps permission and return true and false in result
        return ActivityCompat.checkSelfPermission(
                context , Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context , Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
    }
    fun requestPermission(target:Activity)
    {
        ActivityCompat.requestPermissions(
            target , arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION ,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) , Constants.REQUEST_GPS_CODE
        )
    }
}