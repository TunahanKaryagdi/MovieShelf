package com.tunahankaryagdi.firstproject.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunahankaryagdi.firstproject.data.model.entity.MovieEntity
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.model.MovieDetail
import com.tunahankaryagdi.firstproject.domain.model.Review
import com.tunahankaryagdi.firstproject.domain.use_case.AddToFavoritesUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetDetailByMovieIdUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetReviewsUseCase
import com.tunahankaryagdi.firstproject.ui.home.HomeUiState
import com.tunahankaryagdi.firstproject.utils.DetailTab
import com.tunahankaryagdi.firstproject.utils.HomeTab
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
    private val getReviewsUseCase: GetReviewsUseCase
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

    fun getReviewsByMovieId(movieId: Int) {
        viewModelScope.launch {
            getReviewsUseCase.invoke(movieId).collect { reviews ->
                _uiState.update { current ->
                    current.copy(
                        reviews = reviews
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
)