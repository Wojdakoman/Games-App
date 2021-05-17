package com.example.gameapp.models

import com.example.gameapp.models.api.GamesAPI
import com.example.gameapp.models.api.SafeApiRequest
import com.example.gameapp.models.api.TwitchAPI
import okhttp3.MediaType
import okhttp3.RequestBody
import java.time.Instant
import java.util.*

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

    suspend fun getCovers(ids: String, n: Int) = apiRequest {
        var body = RequestBody.create(MediaType.parse("text/*"), "fields *; where id = ($ids); limit $n;")
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

    suspend fun getNewest() = apiRequest {
        var body = RequestBody.create(MediaType.parse("text/*"), "fields *; sort first_release_date desc; where first_release_date != null & status = 0 & cover != null; limit 20;")
        GamesAPI().getGame(body, access)
    }

    suspend fun getTheBest() = apiRequest {
        var body = RequestBody.create(MediaType.parse("text/*"), "fields *; sort total_rating desc; where cover != null & total_rating != null; limit 20;")
        GamesAPI().getGame(body, access)
    }

    suspend fun getComingSoon() = apiRequest {
        var body = RequestBody.create(MediaType.parse("text/*"),
            "fields *; sort first_release_date asc; where first_release_date != null & status = (2,3,4) & cover != null & first_release_date >= ${Instant.now().epochSecond};")
        GamesAPI().getGame(body, access)
    }

    suspend fun getPlatforms(ids: String, n: Int) = apiRequest {
        var body = RequestBody.create(MediaType.parse("text/*"), "fields *; where id = ($ids); limit $n;")
        GamesAPI().getPlatforms(body, access)
    }

    suspend fun getPlatformLogos(ids: String, n: Int) = apiRequest {
        var body = RequestBody.create(MediaType.parse("text/*"), "fields *; where id = ($ids); limit $n;")
        GamesAPI().getPlatformLogos(body, access)
    }
}