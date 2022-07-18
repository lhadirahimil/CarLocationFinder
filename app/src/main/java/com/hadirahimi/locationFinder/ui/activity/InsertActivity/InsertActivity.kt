package com.hadirahimi.locationFinder.ui.activity.InsertActivity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.carto.styles.AnimationStyle
import com.carto.styles.AnimationStyleBuilder
import com.carto.styles.AnimationType
import com.carto.styles.MarkerStyleBuilder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hadirahimi.locationFinder.DataBase.Model.ModelDataBase
import com.hadirahimi.locationFinder.Utils.DateUtil
import com.hadirahimi.locationFinder.Utils.PermissionManager
import com.hadirahimi.locationFinder.ViewModel.DialogInsertViewModel
import com.hadirahimi.locationFinder.databinding.ActivityInsertBinding
import dagger.hilt.android.AndroidEntryPoint
import org.neshan.common.model.LatLng
import org.neshan.mapsdk.internal.utils.BitmapUtils
import org.neshan.mapsdk.model.Marker
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class InsertActivity : AppCompatActivity()
{
    private lateinit var binding : ActivityInsertBinding
    private lateinit var animSt : AnimationStyle
    private lateinit var locationManager : LocationManager
    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private lateinit var latLng : LatLng
    
    @Inject
    lateinit var locationModel : ModelDataBase
    
    private val viewModel : DialogInsertViewModel by viewModels()
    
    @Inject
    lateinit var dateUtil : DateUtil
    
    
    @Inject
    lateinit var permissionManager : PermissionManager
    
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickEvent()
    }
    
    private fun clickEvent()
    {
        //btn save location in room db
        btnSubmit()
        // dismiss this dialog
        btnBack()
        // find user location button
        btnMyLocation()
    }
    
    
    private fun btnMyLocation()
    {
        //find user location by gps and show on map
        binding.btnFindMyLocation.setOnClickListener {
            //init location manager
            //init fused location provider client
            
            
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            
            //check if permission is granted lets find user latLong else show permission denied
            if (checkPermission())
            {
                //permission is granted lets go
                
                //check gps is on or off
                if (isGpsOn())
                {
                    
                    //request update location
                    getMyLocation()
                    
                }
                else
                {
                    //turn on gps
                    Toast.makeText(this , "لطفا لوکیشن خود را روشن کنید" , Toast.LENGTH_LONG).show()
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                
                
            }
            else
            {
                //permission is denied show message to user
                Toast.makeText(
                    this , "مجوز دسترسی به موقعیت مکانی یافت نشد." , Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun getMyLocation()
    {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this@InsertActivity)
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            //init location
            val location : Location = it.result
            location.let {
                binding.btnSubmit.isEnabled = true
                latLng = LatLng(location.latitude , location.longitude)
            }
            
            
            //initialise map for show latLng
            binding.apply {
                
                //clear all markers
                mapNeshan.clearMarkers()
                
                //show latLng On map
                mapNeshan.moveCamera(latLng , 0f)
                //zoom map on location
                mapNeshan.setZoom(17f , 2.95f)
                
                //add a new marker on map
                mapNeshan.addMarker(createMarker(latLng))
                
            }
            //api request address
            viewModel.getAddress(latLng.latitude.toString() , latLng.longitude.toString())
            viewModel.address.observe(this) { it ->
                it.let {
                    binding.tvMyAddress.text = " آدرس : $it "
                }
            }
            viewModel.loading.observe(this) {
                if (it)
                {
                    binding.tvMyAddress.text = "آدرس : درحال دریافت..."
                }
            }
            
            
        }
    }

//    private var dialogResponse : ((ModelDataBase) -> Unit?)? = null
//
//    fun dialogResult(response : (ModelDataBase) -> Unit)
//    {
//        dialogResponse = response
//    }
    
    private fun btnBack()
    {
        //go back click listener
        binding.btnBack.setOnClickListener { finish() }
    }
    
    private fun btnSubmit()
    {
        binding.btnSubmit.setOnClickListener {
            
            //default id
            locationModel.id = 0
            
            // Address Process
            if (binding.tvMyAddress.text.isNotEmpty() && binding.tvMyAddress.text.toString()
                    .equals("آدرس : درحال دریافت...")) locationModel.address = "نامشخص"
            else locationModel.address =
                binding.tvMyAddress.text.toString().replace("آدرس :استان" , "")
            
            //Lat Long
            latLng.let {
                locationModel.lat = it.latitude
                locationModel.lng = it.longitude
            }
            
            //time
            dateUtil.GregorianToPersian(
                getCurrentDate().substring(0 , 4).toInt() ,
                getCurrentDate().substring(5 , 7).toInt() ,
                getCurrentDate().substring(8 , 10).toInt()
            )
            
            locationModel.date = dateUtil.toString() + getCurrentTime()
            
            
            //push to viewModel and Insert to database
            viewModel.insertLocation(locationModel)
            
            //show result as toast
            // Toast.makeText(this , "لوکیشن با موفقیت ثبت شد" , Toast.LENGTH_SHORT).show()
            
            //close this activity
            finish()
        }
    }
    
    private fun getCurrentDate() : String
    {
        val current = LocalDateTime.now()
        
        
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return current.format(formatter)
    }
    
    private fun getCurrentTime() : String
    {
        val current = LocalDateTime.now()
        
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return current.format(formatter)
    }
    
    private fun checkPermission() : Boolean
    {
        return ActivityCompat.checkSelfPermission(
            this , Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this , Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    private fun isGpsOn() : Boolean
    {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    
    private fun createMarker(loc : LatLng) : Marker
    {
        
        // AnimationStyle
        val animStBl = AnimationStyleBuilder()
        animStBl.fadeAnimationType = AnimationType.ANIMATION_TYPE_SMOOTHSTEP
        animStBl.sizeAnimationType = AnimationType.ANIMATION_TYPE_SPRING
        animStBl.phaseInDuration = 0.5f
        animStBl.phaseOutDuration = 0.5f
        animSt = animStBl.buildStyle()
        
        val markStCr = MarkerStyleBuilder()
        markStCr.size = 30f
        markStCr.bitmap = BitmapUtils.createBitmapFromAndroidBitmap(
            BitmapFactory.decodeResource(
                resources , org.neshan.mapsdk.R.drawable.ic_marker
            )
        )
        // AnimationStyle object - that was created before - is used here
        markStCr.animationStyle = animSt
        val markSt = markStCr.buildStyle()
        
        // Creating marker
        return Marker(loc , markSt)
    }
    
    
}