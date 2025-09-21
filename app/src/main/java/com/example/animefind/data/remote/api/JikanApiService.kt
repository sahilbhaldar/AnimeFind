package com.example.animefind.data.remote.api


import com.example.animefind.data.remote.model.details.AnimeDetailResponse
import com.example.animefind.data.remote.model.AnimeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanApiService {

    // Get top anime list
    @GET("top/anime")
    suspend fun getTopAnime(
        @Query("page") page: Int
    ): AnimeResponse

    // Get anime details by ID
    @GET("anime/{id}")
    suspend fun getAnimeDetails(
        @Path("id") animeId: Int
    ): AnimeDetailResponse
}
