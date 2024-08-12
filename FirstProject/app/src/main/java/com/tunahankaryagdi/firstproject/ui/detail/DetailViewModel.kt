package com.tunahankaryagdi.firstproject.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.tunahankaryagdi.firstproject.data.model.entity.MovieEntity
import com.tunahankaryagdi.firstproject.domain.model.MovieDetail
import com.tunahankaryagdi.firstproject.domain.model.Review
import com.tunahankaryagdi.firstproject.domain.use_case.AddToFavoritesUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.CheckIsFavoriteUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetDetailByMovieIdUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetReviewsUseCase
import com.tunahankaryagdi.firstproject.ui.base.BaseUiState
import com.tunahankaryagdi.firstproject.ui.base.BaseViewModel
import com.tunahankaryagdi.firstproject.utils.DetailTab
import com.tunahankaryagdi.firstproject.utils.ext.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getDetailByMovieIdUseCase: GetDetailByMovieIdUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val getReviewsUseCase: GetReviewsUseCase,
    private val checkIsFavoriteUseCase: CheckIsFavoriteUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<DetailUiState>() {

    override fun createInitialState(): DetailUiState = DetailUiState()

    init {
        savedStateHandle.get<Int>("movieId")?.let { movieId ->
            init(movieId)
        }
    }


    fun init(movieId: Int){
        getDetailByMovieId(movieId)
        getReviewsByMovieId(movieId)
        checkIsFavorite(movieId)
    }

    private fun getDetailByMovieId(movieId: Int) {
        viewModelScope.launch {
            getDetailByMovieIdUseCase.invoke(movieId).collectAndHandle(
                scope = this,
                onSuccess = { data ->
                    _uiState.update { current ->
                        current.copy(
                            movieDetail = data,
                        )
                    }
                },
            )
        }
    }

    private fun getReviewsByMovieId(movieId: Int) {
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

    private fun checkIsFavorite(movieId: Int) {
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
    override val isLoading: Boolean = false,
    val movieDetail: MovieDetail? = null,
    val reviews: List<Review> = emptyList(),
    val selectedTab: DetailTab = DetailTab.ABOUT_MOVIE,
    val isFavorite: Boolean = false
) : BaseUiState()