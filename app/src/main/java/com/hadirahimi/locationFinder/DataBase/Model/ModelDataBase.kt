package com.hadirahimi.locationFinder.DataBase.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hadirahimi.locationFinder.Utils.Constants.CAR_LOCATION


@Entity(tableName = CAR_LOCATION) // the name of table
data class ModelDataBase
    (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int = 0 ,
    var address : String = "" ,
    var lat : Double = 0.0 ,
    var lng : Double = 0.0 ,
    var date : String = ""
)