package com.example.gameapp.models.entities

data class Game(
    val id: Int,
    val artworks: List<Int>,
    val cover: Int,
    val dlcs: List<Int>,
    val first_release_date: Long,
    val game_engines: List<Int>,
    val involved_companies: List<Int>,
    val name: String,
    val parent_game: Int,
    val platforms: List<Int>,
    val total_rating: Double,
    val screenshots: List<Int>,
    val similar_games: List<Int>
)