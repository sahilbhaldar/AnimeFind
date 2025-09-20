package com.example.animefind.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime_table")
data class AnimeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val imageUrl: String?,
    val synopsis: String?,
    val genres: String?,       // comma-separated genre names
    val mainCast: String?,     // comma-separated character/voice actor names
    val trailerUrl: String?,
    val pageNumber:Int?
)
