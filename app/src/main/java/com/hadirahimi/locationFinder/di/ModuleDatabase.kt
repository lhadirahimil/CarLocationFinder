package com.hadirahimi.locationFinder.di

import android.content.Context
import androidx.room.Room
import com.hadirahimi.locationFinder.DataBase.Model.ModelDataBase
import com.hadirahimi.locationFinder.DataBase.MyDatabase
import com.hadirahimi.locationFinder.Utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object ModuleDatabase
{
    @Provides
    fun provideEntity() = ModelDataBase()
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context : Context) =
        Room.databaseBuilder(context , MyDatabase::class.java , Constants.CAR_DATABASE)
            .allowMainThreadQueries().fallbackToDestructiveMigration().build()
    
    @Provides
    @Singleton
    fun provideDao(db : MyDatabase) = db.daoCarLocation()
    
    
}