package com.example.gameapp.models.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.igdb.com/v4/"

object GamesService {
    private val retrofit by lazy {
        val client = OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES).build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: GamesAPI by lazy {
        retrofit.create(GamesAPI::class.java)
    }
}