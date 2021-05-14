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

    suspend fun getAccessToken() = apiRequest {
        TwitchAPI().getAccessToken()
    }

    suspend fun getCover(gameID: Int) = apiRequest {
        var body = RequestBody.create(MediaType.parse("text/*"), "fields *; where game = $gameID;")
        GamesAPI().getCover(body, "Bearer $ACCESS_TOKEN")
    }
}