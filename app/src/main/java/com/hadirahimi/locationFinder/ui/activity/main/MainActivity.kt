package com.hadirahimi.locationFinder.ui.activity.main

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hadirahimi.locationFinder.Utils.Constants
import com.hadirahimi.locationFinder.Utils.PermissionManager
import com.hadirahimi.locationFinder.ViewModel.MainViewModel
import com.hadirahimi.locationFinder.databinding.ActivityMainBinding
import com.hadirahimi.locationFinder.ui.Dialog.DialogConfirmDelete
import com.hadirahimi.locationFinder.ui.activity.InsertActivity.InsertActivity
import com.hadirahimi.locationFinder.ui.activity.main.adapter.AdapterLocation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    private lateinit var binding : ActivityMainBinding
    
    @Inject
    lateinit var adapterLocation : AdapterLocation
    
    val viewModel : MainViewModel by viewModels()
    
    @Inject
    lateinit var permissionManager : PermissionManager
    
    @Inject
    lateinit var dialogDelete : DialogConfirmDelete
    
    
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        viewBinding()
        
        recyclerviewItemClick()
        getData()
        clickEvent()
        viewModel.deleted.observe(this) {
        
            if (it)
            {
                Toast.makeText(this , "لوکیشن با موفقیت حذف شد" , Toast.LENGTH_SHORT).show()
                viewModel.locationLists()
                
            }
            else Toast.makeText(this , "خطا در حذف لوکیشن" , Toast.LENGTH_SHORT).show()
            
        }
        
        
    }
    
    private fun recyclerviewItemClick()
    {
        //start finding route by other map applications
        adapterLocation.setOnGoToLocationCLickListener {
            val lat = it.lat.toString()
            val lng = it.lng.toString()
            startActivity(Intent(Intent.ACTION_VIEW , Uri.parse("geo:0,0?q=$lat,$lng")))
        }
    }
    
    //    private fun getResponseFromDialog()
//    {
//        dialogInsert.dialogResult {
//            viewModel.insertLocation(it)
//        }
//    }
    override fun onResume()
    {
        super.onResume()
        viewModel.locationLists()
    }
    
    private fun getData()
    {
        viewModel.locationLists()
        
        //listen to empty variable and if empty is true so room database is empty
        viewModel.empty.observe(this) { isEmpty ->
            binding.apply {
                if (isEmpty)
                {
        
                    recyLocation.visibility = View.GONE
                    emptyLayout.visibility = View.VISIBLE
                }
                else {
                    emptyLayout.visibility = View.GONE
                    recyLocation.visibility = View.VISIBLE
                }
            }
        }
        
        //receive locations data from view holder
        viewModel.locations.observe(this) { locations ->
            //push data from database to adapter
            adapterLocation.differ.submitList(locations)
            
            // init recyclerview
            binding.apply {
                
                //init layout
                recyLocation.layoutManager = LinearLayoutManager(this@MainActivity)
                
                //pas adapter to recyclerview
                recyLocation.adapter = adapterLocation
                
            }
        }
    }
    
    private fun clickEvent()
    {
        insertClick()
        
    }
    
    private fun insertClick()
    {
        binding.btnSaveParkLoc.setOnClickListener {
            //check gps permission is granted
            if (permissionManager.checkPermission())
            {
                //show insert activity
                showInsertActivity()
            }
            else
            {
                
                permissionManager.requestPermission(this)
                Toast.makeText(this@MainActivity , "request permission" , Toast.LENGTH_SHORT).show()
            }
            
        }
        adapterLocation.setOnDeleteLocationCLickListener { location ->
            //delete selected item from database
            dialogDelete.show()
         //   viewModel.deleteLocationById(location)
            dialogDelete.setOnDeleteClickListener {
                viewModel.deleteLocationById(location)
                dialogDelete.dismiss()
            }
            
            
        }
    }
    
    private fun showInsertActivity()
    {
        startActivity(Intent(this@MainActivity , InsertActivity::class.java))
    }
    
    
    private fun viewBinding()
    {
        //init view Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    
    override fun onRequestPermissionsResult(
        requestCode : Int , permissions : Array<out String> , grantResults : IntArray
    )
    {
        super.onRequestPermissionsResult(requestCode , permissions , grantResults)
        
        //check if permission is granted lets run insert activity
        if (requestCode == Constants.REQUEST_GPS_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            //show insert new location activity
            showInsertActivity()
        }
    }
}