package com.example.gameapp.models

import androidx.lifecycle.MutableLiveData
import com.example.gameapp.models.entities.FirebaseGame
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRepository {
    private val database: DatabaseReference = Firebase.database.reference
    private val auth: FirebaseAuth = Firebase.auth

    fun getAllUserGames() = database.child(auth.currentUser!!.uid)

    fun addGame(game: FirebaseGame){
        getAllUserGames().child(game.game_id.toString()).setValue(game)
    }

    fun wasGamePlayed(gameID: String): MutableLiveData<Boolean>{
        var answer = MutableLiveData<Boolean>()
        getAllUserGames().child(gameID).get().addOnSuccessListener {
            if(it.child("played").exists()){
                answer.postValue(it.child("played").value as Boolean)
            } else answer.postValue(false)
        }
        return answer
    }

    fun isGameFav(gameID: String): MutableLiveData<Boolean>{
        var answer = MutableLiveData<Boolean>()
        getAllUserGames().child(gameID).get().addOnSuccessListener {
            if(it.child("fav").exists()){
                answer.postValue(it.child("fav").value as Boolean)
            } else answer.postValue(false)
        }
        return answer
    }
}