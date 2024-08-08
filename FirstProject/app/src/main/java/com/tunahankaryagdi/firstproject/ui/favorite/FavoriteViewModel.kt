package com.tunahankaryagdi.firstproject.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.use_case.GetFavoriteMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteUiState())
    val uiState: StateFlow<FavoriteUiState>
        get() = _uiState

    init {
        getFavoriteMovies()
    }

    private fun getFavoriteMovies() {
        viewModelScope.launch {
            getFavoriteMoviesUseCase.invoke().collect { movies ->
                _uiState.update { current ->
                    current.copy(
                        movies = movies,
                        filteredMovies = movies
                    )
                }
            }
        }
    }

    fun filterMovies(searchText: String) {
        val filteredMovies = _uiState.value.movies.filter {
            it.title.contains(searchText, ignoreCase = true)
        }
        _uiState.update { current ->
            current.copy(
                filteredMovies = filteredMovies
            )
        }
    }

}

data class FavoriteUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val filteredMovies: List<Movie> = emptyList(),
)