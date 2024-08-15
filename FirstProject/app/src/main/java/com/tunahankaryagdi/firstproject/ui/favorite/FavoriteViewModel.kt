package com.tunahankaryagdi.firstproject.ui.favorite

import androidx.lifecycle.viewModelScope
import com.tunahankaryagdi.firstproject.data.model.entity.MovieEntity
import com.tunahankaryagdi.firstproject.data.model.entity.toMovie
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.model.toMovieEntity
import com.tunahankaryagdi.firstproject.domain.use_case.DeleteFavoriteMovieUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetFavoriteMoviesUseCase
import com.tunahankaryagdi.firstproject.ui.base.BaseUiState
import com.tunahankaryagdi.firstproject.ui.base.BaseViewModel
import com.tunahankaryagdi.firstproject.utils.ext.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val deleteFavoriteMovieUseCase: DeleteFavoriteMovieUseCase
) : BaseViewModel<FavoriteUiState>() {
    override fun createInitialState(): FavoriteUiState = FavoriteUiState()


    fun getFavoriteMovies() {
        setState(getCurrentState().copy(isLoading = true))
        viewModelScope.launch {
            getFavoriteMoviesUseCase.invoke().collect {
                val movies = it.map { it.toMovie() }
                setState(getCurrentState().copy(movies = movies, filteredMovies = movies, isLoading = false))
            }
        }
    }

    fun deleteFavoriteMovie(movie: Movie, showToast: () -> Unit) {

        viewModelScope.launch {
            deleteFavoriteMovieUseCase.invoke(movie.toMovieEntity()).collectAndHandle(
                this,
                onSuccess = {
                    getFavoriteMovies()
                    showToast()
                }
            )
        }
    }

    fun filterMovies(searchText: String) {
        val state = getCurrentState()
        val filteredMovies = state.movies.filter {
            it.title?.contains(searchText, ignoreCase = true) ?: return
        }
        setState(getCurrentState().copy(filteredMovies = filteredMovies))

    }
}

data class FavoriteUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val filteredMovies: List<Movie> = emptyList(),
) : BaseUiState