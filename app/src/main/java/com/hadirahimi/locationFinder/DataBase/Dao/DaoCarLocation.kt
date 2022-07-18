package com.hadirahimi.locationFinder.DataBase.Dao


import android.database.sqlite.SQLiteDatabase
import androidx.room.*
import com.hadirahimi.locationFinder.DataBase.Model.ModelDataBase
import com.hadirahimi.locationFinder.Utils.Constants
import com.hadirahimi.locationFinder.Utils.Constants.CAR_LOCATION

@Dao
interface DaoCarLocation
{
    @Insert(onConflict = SQLiteDatabase.CONFLICT_REPLACE)
    suspend fun insert(data : ModelDataBase)
    
    @Delete
    fun delete(data : ModelDataBase)
    
    @Update
    fun update(data : ModelDataBase) : Int
    
    @Query("SELECT * from $CAR_LOCATION ORDER BY id DESC")
    fun getAll() : List<ModelDataBase>
    
    @Query("SELECT EXISTS (SELECT 1 FROM $CAR_LOCATION WHERE id = :id)")
    suspend fun existsLocation(id:Int):Boolean

}