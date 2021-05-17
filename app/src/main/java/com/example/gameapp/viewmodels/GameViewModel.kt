package com.example.gameapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gameapp.models.GamesRepository
import com.example.gameapp.models.entities.Game
import com.example.gameapp.models.entities.SearchResult
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel(application: Application): AndroidViewModel(application) {
    lateinit var repository: GamesRepository

    val game = MutableLiveData<Game>()
    val cover = MutableLiveData<String>()
    val background = MutableLiveData<String>()
    val isPlayed = MutableLiveData<Boolean>()
    val isFav = MutableLiveData<Boolean>()
    val searchResults = MutableLiveData<List<SearchResult>>()

    fun changeGame(id: Int){
        viewModelScope.launch {
            game.postValue(repository?.getGame(id)[0])
        }
    }

    fun getGameDetails(){
        viewModelScope.launch {
            cover.postValue(repository?.getCover(game.value?.id!!)[0].image_id)
            if(game.value?.artworks != null){
                background.postValue(repository?.getArtwork(game.value?.artworks!![Random.nextInt(0, game.value?.artworks!!.size)])[0].image_id)
            } else if(game.value?.screenshots != null)
                background.postValue(repository?.getScreen(game.value?.screenshots!![Random.nextInt(0, game.value?.screenshots!!.size)])[0].image_id)
        }
    }

    fun search(query: String){
        viewModelScope.launch {
            val results = mutableListOf<SearchResult>()
            for(game in repository?.search(query)){
                val gCover = repository?.getCover(game.id)
                results.add(SearchResult(game.id, game.name, if(gCover.isNotEmpty()) gCover[0].image_id else "null"))
            }
            searchResults.postValue(results)
        }
    }

    fun setDefaultState(played: Boolean = false, fav: Boolean = false){
        isPlayed.postValue(played)
        isFav.postValue(fav)
    }

    fun playedClick(){
        isPlayed.postValue(!isPlayed.value!!)
    }

    fun favClick(){
        isFav.postValue(!isFav.value!!)
    }

    fun getCoverUrl(id: String) = "https://images.igdb.com/igdb/image/upload/t_720p/$id.jpg"
}