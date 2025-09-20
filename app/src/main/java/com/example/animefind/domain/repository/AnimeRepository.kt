package com.example.animefind.domain.repository

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
        val cached = dao.getAnimeByPage(page)
        if (cached.isNotEmpty()) {
            // ✅ Return from DB
            return cached.map { it.toDomainModel() }
        }

        // ✅ No data in DB → fetch from network
        val response = api.getTopAnime(page)

        val entities = response.data.map { it.toEntity(page) }

        // Insert avoiding duplicates
        dao.insertAnimeList(entities)

        return entities.map { it.toDomainModel() }
    }

    suspend fun getAnimeDetails(animeId: Int): Anime {
        return try {
            // Try network first
            val response = api.getAnimeDetails(animeId)
            response.toDomainModel()
        } catch (e: Exception) {
            // If network fails, show error
            throw Exception("Network not available or failed: ${e.message}")
        }
    }


    // For UI observing changes directly from DB
    fun observeAnimeList() = dao.observeAllAnime()
    fun observeAnimeDetails(animeId: Int) = dao.observeAnimeById(animeId)
}
