package com.example.gameapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gameapp.models.GamesRepository
import com.example.gameapp.models.entities.SearchResult
import kotlinx.coroutines.launch

class DiscoverViewModel(application: Application): AndroidViewModel(application) {
    lateinit var repository: GamesRepository

    val showProgress = MutableLiveData<Boolean>()
    //if all categories have been loaded
    private val loaded = mutableListOf(false, false, false)

    fun loadNewest(): MutableLiveData<List<SearchResult>>{
        val list = MutableLiveData<List<SearchResult>>()
        if(!loaded[0]){
            viewModelScope.launch {
                val results = mutableListOf<SearchResult>()
                for(game in repository?.getNewest()){
                    val gCover = repository?.getCover(game.id)
                    results.add(SearchResult(game.id, game.name, if(gCover.isNotEmpty()) gCover[0].image_id else "null"))
                }
                list.postValue(results)
                endLoading(0)
            }
        }
        return list
    }

    fun loadBestGames(): MutableLiveData<List<SearchResult>>{
        val list = MutableLiveData<List<SearchResult>>()
        if(!loaded[1])
        viewModelScope.launch {
            val results = mutableListOf<SearchResult>()
            for(game in repository?.getTheBest()){
                val gCover = repository?.getCover(game.id)
                results.add(SearchResult(game.id, game.name, if(gCover.isNotEmpty()) gCover[0].image_id else "null"))
            }
            list.postValue(results)
            endLoading(1)
        }
        return list
    }

    fun loadComingGames(): MutableLiveData<List<SearchResult>>{
        val list = MutableLiveData<List<SearchResult>>()
        if(!loaded[2])
        viewModelScope.launch {
            val results = mutableListOf<SearchResult>()
            for(game in repository?.getComingSoon()){
                val gCover = repository?.getCover(game.id)
                results.add(SearchResult(game.id, game.name, if(gCover.isNotEmpty()) gCover[0].image_id else "null"))
            }
            list.postValue(results)
            endLoading(2)
        }
        return list
    }

    private fun endLoading(i: Int){
        loaded[i] = true
        for(x in loaded)
            if(!x) return

        showProgress.postValue(false)
    }
}