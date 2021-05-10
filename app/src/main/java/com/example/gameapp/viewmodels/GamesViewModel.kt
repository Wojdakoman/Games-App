package com.example.gameapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameapp.models.GamesRepository
import com.example.gameapp.models.api.GamesAPI
import kotlinx.coroutines.launch

class GamesViewModel(application: Application): AndroidViewModel(application) {
    private val repository = GamesRepository(GamesAPI())

     fun loadAccessToken(){
        viewModelScope.launch {
            repository.ACCESS_TOKEN = repository.getAccessToken().access_token
            Log.d("[TOKEN]", "tplem: ${repository.ACCESS_TOKEN}")
        }
    }
}