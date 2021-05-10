package com.example.gameapp.models.api

import com.example.gameapp.models.entities.TwitchAccessToken
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://id.twitch.tv/oauth2/"
const val CLIENT_ID = "ojpwo04xemos2tfjo6fnswjnyihlcw"
const val CLIENT_SECRET = "5wn77ivxquw7j9i4kcrxgjwfng5as7"
const val API = "?client_id=$CLIENT_ID&client_secret=$CLIENT_SECRET&grant_type=client_credentials"

interface TwitchAPI {
    @POST("token$API")
    suspend fun getAccessToken(): Response<TwitchAccessToken>

    companion object {
        operator fun invoke(): TwitchAPI {
            val client = OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES).build()
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(TwitchAPI::class.java)
        }
    }
}