package com.example.gameapp.models

import com.example.gameapp.models.api.GamesAPI
import com.example.gameapp.models.api.SafeApiRequest
import com.example.gameapp.models.api.TwitchAPI

class GamesRepository(
        private val gamesAPI: GamesAPI,
        var ACCESS_TOKEN: String = "null"
        ): SafeApiRequest() {

    suspend fun getAccessToken() = apiRequest {
        TwitchAPI().getAccessToken()
    }
}