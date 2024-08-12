package com.tunahankaryagdi.firstproject.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tunahankaryagdi.firstproject.data.model.dto.toMovie
import com.tunahankaryagdi.firstproject.domain.error_handling.Resource
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import com.tunahankaryagdi.firstproject.domain.use_case.GetNowPlayingMoviesUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetPopularMoviesUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetTopRatedMoviesUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetUpcomingMoviesUseCase
import com.tunahankaryagdi.firstproject.utils.HomeTab
import com.tunahankaryagdi.firstproject.utils.ext.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase
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
            getPopularMoviesUseCase.invoke().collectAndHandle(
                scope = this,
                onSuccess = { data ->
                    _uiState.update { current ->
                        current.copy(
                            popularMovies = data
                        )
                    }
                }
            )
        }
    }

    private fun getNowPlayingMovies() {
        viewModelScope.launch {
            getNowPlayingMoviesUseCase.invoke().cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _uiState.update { current ->
                        current.copy(
                            movies = pagingData
                        )
                    }
                }
        }
    }

    private fun getTopRatedMovies() {
        viewModelScope.launch {
            getTopRatedMoviesUseCase.invoke().cachedIn(viewModelScope).collectLatest { pagingData ->
                _uiState.update { current ->
                    current.copy(
                        movies = pagingData
                    )
                }
            }
        }
    }

    private fun getUpcomingMovies() {
        viewModelScope.launch {
            getUpcomingMoviesUseCase.invoke().cachedIn(viewModelScope).collectLatest { pagingData ->
                _uiState.update { current ->
                    current.copy(
                        movies = pagingData
                    )
                }
            }
        }
    }


    private fun getMovies() {
        when (_uiState.value.selectedTab) {
            HomeTab.NOW_PLAYING -> getNowPlayingMovies()
            HomeTab.TOP_RATED -> getTopRatedMovies()
            HomeTab.UPCOMING -> getUpcomingMovies()
            else -> {}
        }
    }
}


data class HomeUiState(
    val isLoading: Boolean = false,
    val selectedTab: HomeTab = HomeTab.NOW_PLAYING,
    val popularMovies: List<Movie> = emptyList(),
    val movies: PagingData<Movie> = PagingData.empty()
)
