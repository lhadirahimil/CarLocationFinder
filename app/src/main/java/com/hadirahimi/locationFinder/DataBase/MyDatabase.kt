package com.hadirahimi.locationFinder.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hadirahimi.locationFinder.DataBase.Dao.DaoCarLocation
import com.hadirahimi.locationFinder.DataBase.Model.ModelDataBase


@Database(entities = [ModelDataBase::class] , version = 1 , exportSchema = false)
abstract class MyDatabase : RoomDatabase()
{
    
    abstract fun daoCarLocation() : DaoCarLocation
    
}