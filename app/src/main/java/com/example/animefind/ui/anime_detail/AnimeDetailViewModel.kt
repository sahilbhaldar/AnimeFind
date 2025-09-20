package com.example.animefind.ui.anime_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animefind.domain.model.Anime
import com.example.animefind.domain.repository.AnimeRepository
import com.example.animefind.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnimeDetailViewModel(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _animeDetail = MutableStateFlow<Resource<Anime>>(Resource.Idle())
    val animeDetail: StateFlow<Resource<Anime>> = _animeDetail

    fun fetchAnimeDetail(animeId: Int) {
        viewModelScope.launch {
            _animeDetail.value = Resource.Loading()
            try {
                val anime = repository.getAnimeDetails(animeId)
                _animeDetail.value = Resource.Success(anime)
            } catch (e: Exception) {
                _animeDetail.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }
}
