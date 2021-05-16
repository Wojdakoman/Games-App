package com.example.gameapp.models

import com.example.gameapp.models.api.GamesAPI
import com.example.gameapp.models.api.SafeApiRequest
import com.example.gameapp.models.api.TwitchAPI
import okhttp3.MediaType
import okhttp3.RequestBody

class GamesRepository(
        private val gamesAPI: GamesAPI,
        var ACCESS_TOKEN: String = "null"
        ): SafeApiRequest() {

    private val access: String
    get() = "Bearer $ACCESS_TOKEN"

    suspend fun getAccessToken() = apiRequest {
        TwitchAPI().getAccessToken()
    }

    suspend fun getGame(gameID: Int) = apiRequest {
        var body = RequestBody.create(MediaType.parse("text/*"), "fields *; where id = $gameID;")
        GamesAPI().getGame(body, access)
    }

    suspend fun search(query: String) = apiRequest {
        var body = RequestBody.create(MediaType.parse("text/*"), "fields *; search \"$query\";")
        GamesAPI().getGame(body, access)
    }

    suspend fun getCover(gameID: Int) = apiRequest {
        var body = RequestBody.create(MediaType.parse("text/*"), "fields *; where game = $gameID;")
        GamesAPI().getCover(body, access)
    }

    suspend fun getArtwork(artwork: Int) = apiRequest {
        var body = RequestBody.create(MediaType.parse("text/*"), "fields *; where id = $artwork;")
        GamesAPI().getArtwork(body, access)
    }

    suspend fun getScreen(screen: Int) = apiRequest {
        var body = RequestBody.create(MediaType.parse("text/*"), "fields *; where id = $screen;")
        GamesAPI().getScreen(body, access)
    }
}