package com.example.gameapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gameapp.models.GamesRepository
import kotlinx.coroutines.launch

class GameViewModel(application: Application): AndroidViewModel(application) {
    lateinit var repository: GamesRepository

    fun getCover(): MutableLiveData<String>{
        var coverID = MutableLiveData<String>()
        viewModelScope.launch {
            coverID.postValue(repository?.getCover(1981)[0].image_id)
            //Log.d("[LOOP]", "asd ${repository.getCover(1981)}")
        }
        return  coverID
    }

    fun getCoverUrl(id: String) = "https://images.igdb.com/igdb/image/upload/t_720p/$id.jpg"
}