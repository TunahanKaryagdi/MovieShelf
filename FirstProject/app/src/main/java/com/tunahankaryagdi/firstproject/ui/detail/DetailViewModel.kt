package com.tunahankaryagdi.firstproject.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunahankaryagdi.firstproject.data.model.entity.MovieEntity
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.model.MovieDetail
import com.tunahankaryagdi.firstproject.domain.use_case.AddToFavoritesUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetDetailByMovieIdUseCase
import com.tunahankaryagdi.firstproject.ui.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getDetailByMovieIdUseCase: GetDetailByMovieIdUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState>
        get() = _uiState


    fun getDetailByMovieId(movieId: Int) {
        viewModelScope.launch {
            getDetailByMovieIdUseCase.invoke(movieId).collect { movieDetail ->
                _uiState.update { current ->
                    current.copy(
                        movieDetail = movieDetail
                    )
                }
            }
        }
    }

    fun addToFavorites(movieDetail: MovieDetail) {
        val movieEntity =
            MovieEntity(movieDetail.id, movieDetail.originalTitle, movieDetail.backdropPath)

        viewModelScope.launch {
            addToFavoritesUseCase.invoke(movieEntity).collect { isCompleted ->
                if (isCompleted) println("okey")
            }
        }
    }

}

data class DetailUiState(
    val isLoading: Boolean = false,
    val movieDetail: MovieDetail? = null
)