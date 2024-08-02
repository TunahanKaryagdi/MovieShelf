package com.tunahankaryagdi.firstproject.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunahankaryagdi.firstproject.data.repository.MovieRepository
import com.tunahankaryagdi.firstproject.utils.HomeTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState>
        get() = _uiState


    init {
        getPopularMovies()
        getMovies()
    }

    fun setTab(tab: HomeTab) {
        if (tab == _uiState.value.selectedTab) return
        _uiState.update { current ->
            current.copy(
                selectedTab = tab
            )
        }
        getMovies()
    }

    private fun getPopularMovies() {
        viewModelScope.launch {
            try {
                val response = repository.getPopularMovies()
                val movies = response.results.map {
                    it.backdrop_path
                }
                _uiState.update { current ->
                    current.copy(
                        popularMovies = movies
                    )
                }
            } catch (e: Exception) {
                println("error")
            }
        }
    }


    private fun getNowPlayingMovies() {
        viewModelScope.launch {
            try {
                val response = repository.getPopularMovies()
                val movies = response.results.map {
                    it.backdrop_path
                }
                _uiState.update { current ->
                    current.copy(
                        movies = movies
                    )
                }
            } catch (e: Exception) {
                println("error")
            }
        }
    }

    private fun getMovies() {
        when (_uiState.value.selectedTab) {
            HomeTab.NOW_PLAYING -> {
                getNowPlayingMovies()
            }

            else -> {}
        }
    }
}


data class HomeUiState(
    val isLoading: Boolean = false,
    val selectedTab: HomeTab = HomeTab.NOW_PLAYING,
    val popularMovies: List<String> = emptyList(),
    val movies: List<String> = emptyList(),
)
