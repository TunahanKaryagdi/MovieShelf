package com.tunahankaryagdi.firstproject.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunahankaryagdi.firstproject.data.model.entity.MovieEntity
import com.tunahankaryagdi.firstproject.domain.error_handling.Resource
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.use_case.DeleteFavoriteMovieUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetFavoriteMoviesUseCase
import com.tunahankaryagdi.firstproject.ui.base.BaseUiState
import com.tunahankaryagdi.firstproject.ui.base.BaseViewModel
import com.tunahankaryagdi.firstproject.utils.ext.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val deleteFavoriteMovieUseCase: DeleteFavoriteMovieUseCase
) : BaseViewModel<FavoriteUiState>() {
    override fun createInitialState(): FavoriteUiState = FavoriteUiState()

    init {
        getFavoriteMovies()
    }

    private fun getFavoriteMovies() {
        viewModelScope.launch {
            getFavoriteMoviesUseCase.invoke().collectAndHandle(
                scope = this,
                onSuccess = { data ->
                    _uiState.update { current ->
                        current.copy(
                            movies = data,
                            filteredMovies = data
                        )
                    }
                }
            )
        }
    }

    fun deleteFavoriteMovie(movie: Movie) {
        val movieEntity =
            MovieEntity(movie.id, movie.title, movie.backdropPath)
        viewModelScope.launch {
            deleteFavoriteMovieUseCase.invoke(movieEntity).collectAndHandle(
                this,
                onSuccess = {
                    getFavoriteMovies()
                }
            )
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
    override val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val filteredMovies: List<Movie> = emptyList(),
) : BaseUiState()