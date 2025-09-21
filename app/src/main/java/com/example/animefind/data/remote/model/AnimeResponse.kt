package com.example.animefind.data.remote.model

import com.google.gson.annotations.SerializedName

data class AnimeResponse(
    @SerializedName("pagination")
    val pagination: Pagination,
    @SerializedName("data")
    val data: List<AnimeResponseItem>
)

data class Pagination(
    @SerializedName("last_visible_page")
    val last_visible_page: Int,
    @SerializedName("has_next_page")
    val has_next_page: Boolean,
    @SerializedName("current_page")
    val current_page: Int,
)

data class AnimeResponseItem(
    @SerializedName("mal_id")
    val mal_id: Int,
    @SerializedName("url")
    val url: String?,
    @SerializedName("images")
    val images: AnimeImages?,
    @SerializedName("trailer")
    val trailer: AnimeTrailer?,
    @SerializedName("title")
    val title: String,
    @SerializedName("episodes")
    val episodes: Int?,
    @SerializedName("score")
    val score: Double?,
    @SerializedName("synopsis")
    val synopsis: String?,
    @SerializedName("genres")
    val genres: List<AnimeGenre>?

)

data class AnimeImages(
    @SerializedName("jpg")
    val jpg: AnimeJpg,
    @SerializedName("webp")
    val webp: AnimeWebp?
)

data class AnimeJpg(
    @SerializedName("image_url")
    val image_url: String,
    @SerializedName("small_image_url")
    val small_image_url: String?,
    @SerializedName("large_image_url")
    val large_image_url: String?
)

data class AnimeWebp(
    val image_url: String,
    val small_image_url: String?,
    val large_image_url: String?
)

data class AnimeGenre(
    val mal_id: Int,
    val name: String
)

data class AnimeTrailer(
    val youtube_id: String?,
    val url: String?,
    val embed_url: String?
)
