package com.example.animefind.domain.model

data class Anime(
    val id: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val imageUrl: String?,
    val synopsis: String?,
    val genres: String?,      // comma-separated genres
    val mainCast: String?,    // comma-separated cast names
    val trailerUrl: String?
)
