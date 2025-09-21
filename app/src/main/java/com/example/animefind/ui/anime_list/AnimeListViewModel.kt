package com.example.animefind.ui.anime_list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animefind.data.local.db.AnimeDatabase
import com.example.animefind.domain.model.Anime
import com.example.animefind.domain.repository.AnimeRepository
import com.example.animefind.domain.util.Resource
import com.example.animefind.utils.DbUtils
import com.example.animefind.utils.isConnectedToInternet
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

    private val _isDbEmpty = MutableStateFlow(true)
    val isDbEmpty: StateFlow<Boolean> get() = _isDbEmpty

    private val _isInternetAvailable = MutableStateFlow(false)
    val isInternetAvailable: StateFlow<Boolean> get() = _isInternetAvailable

    private var lastVisiblePage: Int = Int.MAX_VALUE

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

                if (_isInternetAvailable.value) {
                    // assume 25 items per page; if less than 25, this is last page
                    lastVisiblePage = if (anime.size < 25) page else Int.MAX_VALUE
                }
            } catch (e: Exception) {
                _animeList.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun nextPage() {
        val next = _currentPage.value + 1
        if (next <= lastVisiblePage) fetchPage(next)
    }

    fun previousPage() {
        val newPage = if (_currentPage.value > 1) _currentPage.value - 1 else 1
        fetchPage(newPage)
    }
    fun checkDbAndInternet(database: AnimeDatabase, context: Context) {
        viewModelScope.launch {
            _isDbEmpty.value = DbUtils.isDbEmpty(database)
            _isInternetAvailable.value = context.isConnectedToInternet()

        }
    }


}
