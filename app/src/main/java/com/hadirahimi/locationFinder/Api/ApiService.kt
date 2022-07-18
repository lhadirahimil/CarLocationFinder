package com.hadirahimi.locationFinder.Api

import com.hadirahimi.locationFinder.Model.ResponseAddress
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService
{
    @GET("reverse")
    suspend fun getAddress(
        @Header("Api-Key") apiKey : String , @Query("lat") lat : String , @Query("lng") lng : String
    ) : Response<ResponseAddress>
}