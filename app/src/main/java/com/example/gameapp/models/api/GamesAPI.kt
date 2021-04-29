package com.example.gameapp.models.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.igdb.com/v4/"

interface  GamesAPI {
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