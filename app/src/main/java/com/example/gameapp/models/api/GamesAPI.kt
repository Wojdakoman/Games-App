package com.example.gameapp.models.api

import com.example.gameapp.models.entities.Cover
import com.example.gameapp.models.entities.Covers
import com.example.gameapp.models.entities.Game
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.igdb.com/v4/"
private const val CLIENT_ID = "ojpwo04xemos2tfjo6fnswjnyihlcw"

interface  GamesAPI {
    @Headers("Client-ID: $CLIENT_ID")
    @POST("covers")
    suspend fun getCover(@Body body: RequestBody, @Header("Authorization") accessToken: String): Response<List<Cover>>

    @Headers("Client-ID: $CLIENT_ID")
    @POST("games")
    suspend fun getGame(@Body body: RequestBody, @Header("Authorization") accessToken: String): Response<List<Game>>

    companion object {
        operator fun invoke(): GamesAPI {
            val client = OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES).build()
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(GamesAPI::class.java)
        }
    }
}