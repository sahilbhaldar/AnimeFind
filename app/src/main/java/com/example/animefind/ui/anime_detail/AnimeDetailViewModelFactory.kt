package com.example.animefind.ui.anime_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.animefind.data.local.db.AnimeDatabase
import com.example.animefind.data.remote.RetrofitClient
import com.example.animefind.domain.repository.AnimeRepository

class AnimeDetailViewModelFactory(
    private val repository: AnimeRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnimeDetailViewModel::class.java)) {
            return AnimeDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        fun provideFactory(context: android.content.Context): AnimeDetailViewModelFactory {
            val database = AnimeDatabase.getDatabase(context)
            val repository = AnimeRepository(RetrofitClient.apiService, database.animeDao())
            return AnimeDetailViewModelFactory(repository)
        }
    }
}
