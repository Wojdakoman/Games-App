package com.example.gameapp.models

import androidx.lifecycle.MutableLiveData
import com.example.gameapp.models.entities.FirebaseGame
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRepository {
    private val database: DatabaseReference = Firebase.database.reference
    private val auth: FirebaseAuth = Firebase.auth

    private fun getAllUserGames() = database.child(auth.currentUser!!.uid)

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

    fun loadUserGames(): MutableLiveData<List<Pair<Int, Int>>>{
        val answer = MutableLiveData<List<Pair<Int, Int>>>()
        val list = mutableListOf<Pair<Int, Int>>()
        getAllUserGames().addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for(child in snapshot.children){
                    /**
                     * MODE:
                     *  played & fav = 0
                     *  played = 1
                     *  fav = 2
                     *  none = 3
                     */
                    val mode: Int =
                        if(child.child("played").value.toString() == "true" && child.child("fav").value.toString() == "true") 0
                        else if(child.child("played").value.toString() == "true") 1
                        else if(child.child("fav").value.toString() == "true") 2
                        else 3
                    list.add(Pair(child.child("game_id").value.toString().toInt(), mode))
                }
                answer.postValue(list)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        return answer
    }

    fun logOut() = auth.signOut()
}