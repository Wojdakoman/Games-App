package com.example.gameapp.models.entities

data class TwitchAccessToken(
        val access_token: String,
        val expires_in: Int,
        val token_type: String
)