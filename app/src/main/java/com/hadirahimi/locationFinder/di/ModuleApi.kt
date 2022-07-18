package com.hadirahimi.locationFinder.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hadirahimi.locationFinder.Api.ApiService
import com.hadirahimi.locationFinder.Utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleApi
{
    @Provides
    @Singleton
    fun provideBaseUrl() = Constants.BASE_URL
    
    
    @Provides
    @Singleton
    fun provideGson() : Gson = GsonBuilder().setLenient().create()
    
    @Provides
    @Singleton
    fun provideConnectionTime() = Constants.CONNECTION_TIME
    
    @Provides
    @Singleton
    fun provideClient(time : Long) = OkHttpClient.Builder().writeTimeout(time , TimeUnit.SECONDS)
        .readTimeout(time , TimeUnit.SECONDS).connectTimeout(time , TimeUnit.SECONDS)
        .connectionPool(
            ConnectionPool(0 , 5 , TimeUnit.MINUTES)
        ).protocols(listOf(Protocol.HTTP_1_1)).build()
    
    @Provides
    @Singleton
    fun provideRetrofit(baseUrl : String , gson : Gson , client : OkHttpClient) : ApiService =
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(gson))
            .client(client).build().create(ApiService::class.java)
}