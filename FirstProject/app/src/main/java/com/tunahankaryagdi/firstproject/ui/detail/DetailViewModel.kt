package com.tunahankaryagdi.firstproject.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunahankaryagdi.firstproject.data.model.entity.MovieEntity
import com.tunahankaryagdi.firstproject.domain.error_handling.Resource
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.model.MovieDetail
import com.tunahankaryagdi.firstproject.domain.model.Review
import com.tunahankaryagdi.firstproject.domain.use_case.AddToFavoritesUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.CheckIsFavoriteUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetDetailByMovieIdUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetReviewsUseCase
import com.tunahankaryagdi.firstproject.ui.home.HomeUiState
import com.tunahankaryagdi.firstproject.utils.DetailTab
import com.tunahankaryagdi.firstproject.utils.HomeTab
import com.tunahankaryagdi.firstproject.utils.ext.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getDetailByMovieIdUseCase: GetDetailByMovieIdUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val getReviewsUseCase: GetReviewsUseCase,
    private val checkIsFavoriteUseCase: CheckIsFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState>
        get() = _uiState


    fun getDetailByMovieId(movieId: Int) {
        viewModelScope.launch {
            getDetailByMovieIdUseCase.invoke(movieId).collectAndHandle(
                scope = this,
                onSuccess = { data ->
                    _uiState.update { current ->
                        current.copy(
                            movieDetail = data
                        )
                    }
                },
                onError = {}
            )
        }
    }

    fun getReviewsByMovieId(movieId: Int) {
        viewModelScope.launch {
            getReviewsUseCase.invoke(movieId).collectAndHandle(
                scope = this,
                onSuccess = { data ->
                    _uiState.update { current ->
                        current.copy(
                            reviews = data
                        )
                    }
                },
                onError = {}
            )


        }
    }

    fun addToFavorites(movieDetail: MovieDetail) {
        val movieEntity =
            MovieEntity(movieDetail.id, movieDetail.originalTitle, movieDetail.backdropPath)

        viewModelScope.launch {
            addToFavoritesUseCase.invoke(movieEntity).collectAndHandle(
                scope = this,
                onSuccess = { checkIsFavorite(movieDetail.id) }
            )
        }
    }

    fun checkIsFavorite(movieId: Int) {
        viewModelScope.launch {
            checkIsFavoriteUseCase.invoke(movieId).collectAndHandle(
                scope = this,
                onSuccess = { data ->
                    _uiState.update { current ->
                        current.copy(
                            isFavorite = data
                        )
                    }
                }
            )
        }
    }

    fun setTab(tab: DetailTab) {
        if (tab == _uiState.value.selectedTab) return
        _uiState.update { current ->
            current.copy(
                selectedTab = tab
            )
        }
    }


}

data class DetailUiState(
    val isLoading: Boolean = false,
    val movieDetail: MovieDetail? = null,
    val reviews: List<Review> = emptyList(),
    val selectedTab: DetailTab = DetailTab.ABOUT_MOVIE,
    val isFavorite: Boolean = false
)