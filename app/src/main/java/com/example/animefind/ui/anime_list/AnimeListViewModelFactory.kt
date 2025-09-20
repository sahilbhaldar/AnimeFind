package com.example.animefind.ui.anime_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.animefind.data.local.db.AnimeDatabase
import com.example.animefind.data.remote.RetrofitClient
import com.example.animefind.data.remote.api.JikanApiService
import com.example.animefind.domain.repository.AnimeRepository

class AnimeListViewModelFactory(
    private val api: JikanApiService = RetrofitClient.apiService,
    private val database: AnimeDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnimeListViewModel::class.java)) {
            val repository = AnimeRepository(api, database.animeDao())
            @Suppress("UNCHECKED_CAST")
            return AnimeListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
