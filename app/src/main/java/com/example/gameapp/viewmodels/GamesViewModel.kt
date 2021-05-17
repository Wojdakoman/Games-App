package com.example.gameapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gameapp.models.FirebaseRepository
import com.example.gameapp.models.GamesRepository
import com.example.gameapp.models.api.GamesAPI
import com.example.gameapp.models.entities.Game
import kotlinx.coroutines.launch

class GamesViewModel(application: Application): AndroidViewModel(application) {
    lateinit var repository: GamesRepository
    private val firebase = FirebaseRepository()

    fun getGames(){
        firebase.loadUserGames().observeForever{
            for(x in it)
                Log.d("[LIST]", "i: ${x.first} ${x.second}")
        }
    }
}