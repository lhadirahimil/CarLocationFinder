package com.hadirahimi.locationFinder.repository

import com.hadirahimi.locationFinder.Api.ApiService
import com.hadirahimi.locationFinder.DataBase.Dao.DaoCarLocation
import com.hadirahimi.locationFinder.DataBase.Model.ModelDataBase
import com.hadirahimi.locationFinder.Utils.Constants
import javax.inject.Inject

class DialogInsertRepository @Inject constructor(private val api : ApiService , private val dao : DaoCarLocation ,)
{
    suspend fun AddressByLatLng(lat : String , lng : String) =
        api.getAddress(Constants.API_KEY , lat , lng)
    
    suspend fun insertLocation(location : ModelDataBase) = dao.insert(location)
}