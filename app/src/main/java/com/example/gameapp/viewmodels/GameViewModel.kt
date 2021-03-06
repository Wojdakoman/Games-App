package com.example.gameapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gameapp.models.FirebaseRepository
import com.example.gameapp.models.GamesRepository
import com.example.gameapp.models.entities.FirebaseGame
import com.example.gameapp.models.entities.Game
import com.example.gameapp.models.entities.SearchResult
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel(application: Application): AndroidViewModel(application) {
    lateinit var repository: GamesRepository
    val firebase = FirebaseRepository()

    val game = MutableLiveData<Game>()
    val cover = MutableLiveData<String>()
    val background = MutableLiveData<String>()
    val isPlayed = MutableLiveData<Boolean>()
    val isFav = MutableLiveData<Boolean>()
    val searchResults = MutableLiveData<List<SearchResult>>()
    val platforms = MutableLiveData<List<String>>()
    val creators = MutableLiveData<List<String>>()
    val showProgress = MutableLiveData<Boolean>()
    var listMode: Int = 0 //0 - fav list, 1 - played list, 2 - search list
    private var searchQuery = ""

    //method used in GameFragment
    fun changeGame(id: Int){
        viewModelScope.launch {
            showProgress.postValue(true)
            game.postValue(repository?.getGame(id)[0])
        }
    }
    fun getGameDetails(){
        viewModelScope.launch {
            cover.postValue(repository?.getCover(game.value?.id!!)[0].image_id)
            //background
            if(game.value?.artworks != null){
                background.postValue(repository?.getArtwork(game.value?.artworks!![Random.nextInt(0, game.value?.artworks!!.size)])[0].image_id)
            } else if(game.value?.screenshots != null)
                background.postValue(repository?.getScreen(game.value?.screenshots!![Random.nextInt(0, game.value?.screenshots!!.size)])[0].image_id)

            //platforms
            //   - get platforms objects
            val platformsList = repository?.getPlatforms(game.value?.platforms?.joinToString(separator = ",")!!, game.value?.platforms?.size?:0)
            //   - get platforms logos id
            val platformsImages = mutableListOf<Int>()
            for(platform in platformsList)
                platformsImages.add(platform.platform_logo)
            //   - get logos url
            val logos = mutableListOf<String>()
            for(image in repository?.getPlatformLogos(platformsImages.joinToString(separator = ","), platformsImages.size))
                logos.add(image.image_id)
            platforms.postValue(logos)

            //creators
            //   - get involved companies objects
            val invComp = repository?.getCreators(game.value?.involved_companies?.joinToString(separator = ",")!!, game.value?.involved_companies?.size?:0)
            //   - get companies objects
            val companiesIds = mutableListOf<Int>()
            for(comp in invComp)
                companiesIds.add(comp.company)

            val companies = repository?.getCompany(companiesIds.joinToString(separator = ","), companiesIds.size)
            //   - get companies logos
            val compLogosIds = mutableListOf<Int>()
            for(comp in companies)
                compLogosIds.add(comp.logo)
            val compLogos = repository.getCompanyLogos(compLogosIds.joinToString(separator = ","), compLogosIds.size)
            //   - get logos urls
            val compLogoUrl = mutableListOf<String>()
            for(logo in compLogos)
                compLogoUrl.add(logo.image_id)
            creators.postValue(compLogoUrl)

            showProgress.postValue(false)
        }
    }
    //method that performs searching
    fun search(query: String){
        if(searchQuery == query) return //do not download the same data again
        searchQuery = query
        viewModelScope.launch {
            showProgress.postValue(true)
            val results = mutableListOf<SearchResult>()
            for(game in repository?.search(query)){
                val gCover = repository?.getCover(game.id)
                //creators
                //   - get involved companies objects
                val invComp = repository?.getCreators(game.involved_companies.joinToString(separator = ",")!!, game.involved_companies.size)
                //   - get companies objects
                val companiesIds = mutableListOf<Int>()
                for(comp in invComp)
                    companiesIds.add(comp.company)
                val companies = repository?.getCompany(companiesIds.joinToString(separator = ","), companiesIds.size)

                var compNames = mutableListOf<String>()
                for(comp in companies)
                    compNames.add(comp.name)
                results.add(SearchResult(game.id, game.name, if(gCover.isNotEmpty()) gCover[0].image_id else "null", compNames.joinToString(separator = ", ")))
            }
            showProgress.postValue(false)
            searchResults.postValue(results)
        }
    }
    /**
     * gets data from Firebase database
     * sets state of "isPlayed" and "isFav"
     */
    fun setDefaultState(gameId: Int){
        val responseP = firebase.wasGamePlayed(gameId.toString())
        responseP.observeForever {
            isPlayed.postValue(it)
        }
        val responseF = firebase.isGameFav(gameId.toString())
        responseF.observeForever {
            isFav.postValue(it)
        }
    }
    //method called when user clicked "add/remove to played" button
    fun playedClick(){
        firebase.addGame(FirebaseGame(game.value!!.id, !isPlayed.value!!, isFav.value!!))
        isPlayed.postValue(!isPlayed.value!!)
    }
    //method called when user clicked "add/remove to favourite" button
    fun favClick(){
        firebase.addGame(FirebaseGame(game.value!!.id, isPlayed.value!!, !isFav.value!!))
        isFav.postValue(!isFav.value!!)
    }
    //load list of saved games from Firebase
    fun getGames(){
        showProgress.postValue(true)
        //get data from firebase
        firebase.loadUserGames().observeForever{
            val list = mutableListOf<Int>() //list of games ids
            for(x in it)
                if(x.second == 0 || x.second == listMode+1){
                    list.add(x.first)
                } else if(x.second == 3){
                    firebase.deleteGame(x.first.toString())
                }
            loadGames(list)
        }
    }
    //download games details from api
    private fun loadGames(ids: List<Int>){
        val results = mutableListOf<SearchResult>()
        //get games by ids
        viewModelScope.launch {
            for(id in ids){
                val gCover = repository?.getCover(id)
                val game = repository?.getGame(id)[0]
                results.add(SearchResult(game.id, game.name, if(gCover.isNotEmpty()) gCover[0].image_id else "null", "null"))
            }
            searchResults.postValue(results)
            showProgress.postValue(false)
        }
    }

    fun getCoverUrl(id: String) = "https://images.igdb.com/igdb/image/upload/t_720p/$id.jpg"
}