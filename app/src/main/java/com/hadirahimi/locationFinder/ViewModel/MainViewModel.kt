package com.hadirahimi.locationFinder.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadirahimi.locationFinder.DataBase.Model.ModelDataBase
import com.hadirahimi.locationFinder.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val repository : MainRepository) : ViewModel()
{
    val locations = MutableLiveData<List<ModelDataBase>>()
    val empty = MutableLiveData<Boolean>()
    val deleted = MutableLiveData<Boolean>()
    
    
    fun locationLists()
    {
        viewModelScope.launch {
            val list = repository.allLocation()
            if (list.isNotEmpty())
            {
                empty.postValue(false)
                locations.postValue(list)
            }
            else
            {
                empty.postValue(true)
            }
        }
    }
    
    suspend fun exists(id : Int) = withContext(viewModelScope.coroutineContext) {
        repository.existsInFavorite(id)
    }
    
    fun deleteLocationById(location:ModelDataBase) = viewModelScope.launch {
        repository.deleteItem(location)
        val exists = repository.existsInFavorite(location.id)
        if (! exists) deleted.postValue(true)
        else deleted.postValue(false)
    }
    
    
}