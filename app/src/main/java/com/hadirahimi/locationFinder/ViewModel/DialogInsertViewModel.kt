package com.hadirahimi.locationFinder.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadirahimi.locationFinder.DataBase.Model.ModelDataBase
import com.hadirahimi.locationFinder.repository.DialogInsertRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialogInsertViewModel @Inject constructor(val repository : DialogInsertRepository) : ViewModel()
{
    
    val address = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    
   
    
    fun getAddress(lat : String , lng : String) = viewModelScope.launch {
        loading.postValue(true)
        val response = repository.AddressByLatLng(lat , lng)
        
        if (response.isSuccessful)
        {
            val _address = response.body()?.formattedAddress.toString()
            address.postValue(_address)
        }else
            address.postValue("نامشخص")
        loading.postValue(false)
    }
    
    
    fun insertLocation(location : ModelDataBase) = viewModelScope.launch {
        repository.insertLocation(location)
    }
    
    
}