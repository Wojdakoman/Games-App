package com.example.gameapp.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameapp.models.GamesRepository
import com.example.gameapp.models.api.GamesAPI
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    val repository = GamesRepository(GamesAPI())
    var token = MutableLiveData<String>()

    fun loadAccessToken(access_token: String?){
        Log.d("[TOKEN_SAVED]", "ts: $access_token")
        if(access_token.isNullOrEmpty()){
            viewModelScope.launch {
                repository.ACCESS_TOKEN = repository.getAccessToken().access_token
                token.postValue(repository.ACCESS_TOKEN)
                Log.d("[TOKEN]", "token got: ${repository.ACCESS_TOKEN}")
            }
        } else {
            repository.ACCESS_TOKEN = access_token
            Log.d("[TOKEN]", "token saved: ${repository.ACCESS_TOKEN}")
        }

    }
}