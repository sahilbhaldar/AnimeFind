package com.example.animefind.utils

import com.example.animefind.data.local.model.AnimeEntity
import com.example.animefind.data.remote.model.AnimeResponseItem
import com.example.animefind.data.remote.model.details.AnimeDetailResponse
import com.example.animefind.domain.model.Anime

// DB -> Domain
fun AnimeEntity.toDomainModel(): Anime {
    return Anime(
        id = id,
        title = title,
        episodes = episodes,
        score = score,
        imageUrl = imageUrl,
        synopsis = synopsis,
        genres = genres,
        mainCast = mainCast,
        trailerUrl = trailerUrl
    )
}

// Domain -> DB
fun AnimeResponseItem.toEntity(pageNumber: Int): AnimeEntity {
    return AnimeEntity(
        id = mal_id,
        title = title,
        episodes = episodes ?: 0,
        score = score ?: 0.0,
        imageUrl = images?.jpg?.image_url.orEmpty(),
        synopsis = synopsis.orEmpty(),
        genres = genres?.joinToString(", ") { it.name }.orEmpty(),
        mainCast = null,
        trailerUrl = trailer?.url ?: trailer?.youtube_id?.let { "https://www.youtube.com/watch?v=$it" }.orEmpty(),
        pageNumber = pageNumber
    )
}

// Map API detail response to domain model
fun AnimeDetailResponse.toDomainModel(): Anime {
    val data = this.data
    return Anime(
        id = data.mal_id,
        title = data.title,
        episodes = data.episodes ?: 0,
        score = data.score ?: 0.0,
        imageUrl = data.images?.jpg?.image_url.orEmpty(),
        synopsis = data.synopsis.orEmpty(),
        genres = data.genres?.joinToString(", ") { it.name }.orEmpty(),
        mainCast = data.characters?.data?.joinToString(", ") { it.name }.orEmpty(),
        trailerUrl = data.trailer?.url ?: data.trailer?.youtube_id?.let { "https://www.youtube.com/watch?v=$it" }.orEmpty()
    )
}