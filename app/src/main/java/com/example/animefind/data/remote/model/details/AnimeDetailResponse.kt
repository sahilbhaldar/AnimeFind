package com.example.animefind.data.remote.model.details

import com.google.gson.annotations.SerializedName

data class AnimeDetailResponse(
    @SerializedName("data")
    val data: AnimeDetailData
)

data class AnimeDetailData(
    val mal_id: Int,
    val title: String,
    val synopsis: String?,
    val episodes: Int?,
    val score: Double?,
    val trailer: Trailer?,
    val genres: List<Genre>?,
    val characters: CharactersResponse?, // optional if API provides main cast
    @SerializedName("images")
    val images: Images?
)

data class Trailer(
    val youtube_id: String?,
    val url: String?
)

data class Genre(
    val name: String
)

data class CharactersResponse(
    val data: List<CharacterData>?
)

data class CharacterData(
    val name: String
)

data class Images(
    @SerializedName("jpg")
    val jpg: ImageUrls?
)

data class ImageUrls(
    val image_url: String?
)

