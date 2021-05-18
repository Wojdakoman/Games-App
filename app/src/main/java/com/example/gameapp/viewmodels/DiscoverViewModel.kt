package com.example.gameapp.viewmodels

import android.app.Application
import android.util.Log
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
        viewModelScope.launch {
            val results = mutableListOf<SearchResult>()
            val coversIds = mutableListOf<Int>()
            for(game in repository?.getNewest()){
                coversIds.add(game.cover)
                results.add(SearchResult(game.id, game.name, "null", "null"))
            }

            for(cover in repository?.getCovers(coversIds.joinToString(separator = ","), coversIds.size)){
                results[coversIds.indexOf(cover.id)].cover = cover.image_id
            }
            list.postValue(results)
            endLoading(0)
        }
        return list
    }

    fun loadBestGames(): MutableLiveData<List<SearchResult>>{
        val list = MutableLiveData<List<SearchResult>>()
        viewModelScope.launch {
            val results = mutableListOf<SearchResult>()
            val coversIds = mutableListOf<Int>()
            for(game in repository?.getTheBest()){
                coversIds.add(game.cover)
                results.add(SearchResult(game.id, game.name, "null", "null"))
            }

            for(cover in repository?.getCovers(coversIds.joinToString(separator = ","), coversIds.size)){
                results[coversIds.indexOf(cover.id)].cover = cover.image_id
            }
            list.postValue(results)
            endLoading(1)
        }
        return list
    }

    fun loadComingGames(): MutableLiveData<List<SearchResult>>{
        val list = MutableLiveData<List<SearchResult>>()
        viewModelScope.launch {
            val results = mutableListOf<SearchResult>()
            val coversIds = mutableListOf<Int>()
            for(game in repository?.getComingSoon()){
                coversIds.add(game.cover)
                results.add(SearchResult(game.id, game.name, "null", "null"))
            }

            for(cover in repository?.getCovers(coversIds.joinToString(separator = ","), coversIds.size)){
                results[coversIds.indexOf(cover.id)].cover = cover.image_id
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