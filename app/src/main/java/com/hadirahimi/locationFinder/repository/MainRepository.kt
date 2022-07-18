package com.hadirahimi.locationFinder.repository

import com.hadirahimi.locationFinder.Api.ApiService
import com.hadirahimi.locationFinder.DataBase.Dao.DaoCarLocation
import com.hadirahimi.locationFinder.DataBase.Model.ModelDataBase
import com.hadirahimi.locationFinder.Utils.Constants
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val dao : DaoCarLocation ,
    private val api : ApiService
)
{
    fun allLocation() = dao.getAll()
    
    
    
    suspend fun AddressByLatLng(lat : String , lng : String) =
        api.getAddress(Constants.API_KEY , lat , lng)
    
    suspend fun deleteItem(location:ModelDataBase) = dao.delete(location)
    suspend fun existsInFavorite(id : Int) = dao.existsLocation(id)
    
    
}