package com.example.animefind.data.local.db

import androidx.room.*
import com.example.animefind.data.local.model.AnimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    // Insert or update anime list
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(animeList: List<AnimeEntity>)

    // Insert or update single anime (for details)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: AnimeEntity)

    // Get all anime as a list (one-time)
    @Query("SELECT * FROM anime_table ORDER BY score DESC")
    suspend fun getAllAnime(): List<AnimeEntity>

    // Get all anime as a flow (for observing in ViewModel/UI)
    @Query("SELECT * FROM anime_table ORDER BY score DESC")
    fun observeAllAnime(): Flow<List<AnimeEntity>>

    // Get anime by ID (one-time)
    @Query("SELECT * FROM anime_table WHERE id = :animeId")
    suspend fun getAnimeById(animeId: Int): AnimeEntity?

    // Get anime by ID as Flow
    @Query("SELECT * FROM anime_table WHERE id = :animeId")
    fun observeAnimeById(animeId: Int): Flow<AnimeEntity?>

    // Delete all anime
    @Query("DELETE FROM anime_table")
    suspend fun clearAnimeTable()

    @Query("SELECT * FROM anime_table WHERE pageNumber = :page")
    suspend fun getAnimeByPage(page: Int): List<AnimeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeList(anime: List<AnimeEntity>)
}
