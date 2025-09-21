package com.example.animefind.domain.repository

import android.util.Log
import com.example.animefind.data.local.db.AnimeDao
import com.example.animefind.data.remote.api.JikanApiService
import com.example.animefind.domain.model.Anime
import com.example.animefind.utils.toDomainModel
import com.example.animefind.utils.toEntity

class AnimeRepository(
    private val api: JikanApiService,
    private val dao: AnimeDao
) {

    suspend fun getTopAnime(page: Int): List<Anime> {
        // 1. First check DB
        val cached = dao.getAnimeByPage(page)
        if (cached.isNotEmpty()) {
            Log.d("AnimeRepository", "Loaded ${cached.size} anime from DB (page $page)")
            return cached.map { it.toDomainModel() }
        }

        // 2. Try API
        return try {
            Log.d("AnimeRepository", "Fetching anime from API for page $page...")
            val response = api.getTopAnime(page)

            if (response.data.isNullOrEmpty()) {
                Log.w("AnimeRepository", "API returned empty list for page $page")
                return emptyList()
            }

            val entities = response.data.map { it.toEntity(page) }
            dao.insertAnimeList(entities)
            Log.d("AnimeRepository", "Inserted ${entities.size} anime into DB (page $page)")
            entities.map { it.toDomainModel() }
        } catch (e: Exception) {
            Log.e("AnimeRepository", "API call failed: ${e.message}", e)
            emptyList() // fallback to empty if API fails
        }
    }

    suspend fun getAnimeDetails(animeId: Int): Anime {
        return try {
            val response = api.getAnimeDetails(animeId)
            response.toDomainModel()
        } catch (e: Exception) {
            throw Exception("Network not available or failed: ${e.message}")
        }
    }

}
