package com.tunahankaryagdi.firstproject.ui.detail

import androidx.lifecycle.viewModelScope
import com.tunahankaryagdi.firstproject.data.model.entity.MovieEntity
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.model.MovieDetail
import com.tunahankaryagdi.firstproject.domain.model.Review
import com.tunahankaryagdi.firstproject.domain.model.toMovieEntity
import com.tunahankaryagdi.firstproject.domain.use_case.AddToFavoritesUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.CheckIsFavoriteUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.DeleteFavoriteMovieUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetDetailByMovieIdUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetReviewsUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetSimilarMoviesUseCase
import com.tunahankaryagdi.firstproject.ui.base.BaseUiState
import com.tunahankaryagdi.firstproject.ui.base.BaseViewModel
import com.tunahankaryagdi.firstproject.utils.enums.DetailTab
import com.tunahankaryagdi.firstproject.utils.ext.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getDetailByMovieIdUseCase: GetDetailByMovieIdUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val getReviewsUseCase: GetReviewsUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase,
    private val checkIsFavoriteUseCase: CheckIsFavoriteUseCase,
    private val deleteFavoriteMovieUseCase: DeleteFavoriteMovieUseCase,
) : BaseViewModel<DetailUiState>() {

    override fun createInitialState(): DetailUiState = DetailUiState()


    fun init(movieId: Int, onError: (String)-> Unit) {
        getDetailByMovieId(movieId, onError)
        getReviewsByMovieId(movieId,onError)
        getSimilarMoviesByMovieId(movieId,onError)
        checkIsFavorite(movieId)
    }

    private fun getDetailByMovieId(movieId: Int,onError: (String)-> Unit) {
        setState(getCurrentState().copy(isLoading = true))
        viewModelScope.launch {
            getDetailByMovieIdUseCase.invoke(movieId).collectAndHandle(
                scope = this,
                onSuccess = { data ->
                    setState(getCurrentState().copy(movieDetail = data, isLoading = false))
                },
                onError = { error ->
                    onError(error.toString())
                    setState(getCurrentState().copy(isLoading = false))
                }
            )
        }
    }

    private fun getReviewsByMovieId(movieId: Int, onError: (String)-> Unit) {
        viewModelScope.launch {
            getReviewsUseCase.invoke(movieId).collectAndHandle(
                scope = this,
                onSuccess = { data ->
                    setState(getCurrentState().copy(reviews = data))
                },
                onError = { error ->
                    onError(error.toString())
                }
            )
        }
    }

    private fun getSimilarMoviesByMovieId(movieId: Int, onError: (String)-> Unit) {
        viewModelScope.launch {
            getSimilarMoviesUseCase.invoke(movieId).collectAndHandle(
                scope = this,
                onSuccess = { data ->
                    setState(getCurrentState().copy(similarMovies = data))
                },
                onError = { error ->
                    onError(error.toString())
                }
            )
        }
    }

    fun addToFavorites(movieDetail: MovieDetail, showToast: () -> Unit) {
        viewModelScope.launch {
            addToFavoritesUseCase.invoke(movieDetail.toMovieEntity()).collectAndHandle(
                scope = this,
                onSuccess = {
                    checkIsFavorite(movieDetail.id)
                    showToast()
                }
            )
        }
    }

    fun deleteFavorite(movieDetail: MovieDetail, showToast: () -> Unit) {

        viewModelScope.launch {
            deleteFavoriteMovieUseCase.invoke(movieDetail.toMovieEntity()).collectAndHandle(
                scope = this,
                onSuccess = {
                    checkIsFavorite(movieDetail.id)
                    showToast()
                }
            )
        }
    }

    private fun checkIsFavorite(movieId: Int) {
        viewModelScope.launch {
            checkIsFavoriteUseCase.invoke(movieId).collectAndHandle(
                scope = this,
                onSuccess = { data ->
                    setState(getCurrentState().copy(isFavorite = data))

                }
            )
        }
    }

    fun setTab(tab: DetailTab) {
        if (tab == getCurrentState().selectedTab) return
        setState(getCurrentState().copy(selectedTab = tab))

    }


}

data class DetailUiState(
    val isLoading: Boolean = false,
    val movieDetail: MovieDetail? = null,
    val reviews: List<Review> = emptyList(),
    val similarMovies: List<Movie> = emptyList(),
    val selectedTab: DetailTab = DetailTab.ABOUT_MOVIE,
    val isFavorite: Boolean = false
) : BaseUiState