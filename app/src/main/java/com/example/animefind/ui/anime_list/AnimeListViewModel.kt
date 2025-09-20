package com.example.animefind.ui.anime_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animefind.domain.model.Anime
import com.example.animefind.domain.repository.AnimeRepository
import com.example.animefind.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnimeListViewModel(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _animeList = MutableStateFlow<Resource<List<Anime>>>(Resource.Idle())
    val animeList: StateFlow<Resource<List<Anime>>> get() = _animeList

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> get() = _currentPage

    init {
        fetchPage(1)
    }

    fun fetchPage(page: Int) {
        viewModelScope.launch {
            _currentPage.value = page
            _animeList.value = Resource.Loading()
            try {
                val anime = repository.getTopAnime(page) // now DB+API logic
                _animeList.value = Resource.Success(anime)
            } catch (e: Exception) {
                _animeList.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun nextPage() {
        fetchPage(_currentPage.value + 1)
    }

    fun previousPage() {
        val newPage = if (_currentPage.value > 1) _currentPage.value - 1 else 1
        fetchPage(newPage)
    }
}
