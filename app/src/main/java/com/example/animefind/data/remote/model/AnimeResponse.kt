package com.example.animefind.data.remote.model


data class AnimeResponse(
    val data: List<AnimeResponseItem>
)

data class AnimeResponseItem(
    val mal_id: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val images: AnimeImages?,
    val synopsis: String?,
    val genres: List<AnimeGenre>?,
    val trailer: AnimeTrailer?
)

data class AnimeImages(
    val jpg: AnimeJpg
)

data class AnimeJpg(
    val image_url: String
)

data class AnimeGenre(
    val name: String
)

data class AnimeTrailer(
    val url: String?,
    val youtube_id: String?
)
