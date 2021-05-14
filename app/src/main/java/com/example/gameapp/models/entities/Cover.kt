package com.example.gameapp.models.entities

data class Cover(
    val id: Int,
    val game: Int,
    val height: Int,
    val image_id: String,
    val url: String,
    val width: Int
)