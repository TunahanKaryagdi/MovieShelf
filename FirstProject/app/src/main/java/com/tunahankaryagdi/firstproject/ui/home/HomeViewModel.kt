package com.tunahankaryagdi.firstproject.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tunahankaryagdi.firstproject.data.model.dto.toMovie
import com.tunahankaryagdi.firstproject.domain.error_handling.Resource
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.model.SearchMovie
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import com.tunahankaryagdi.firstproject.domain.use_case.GetMoviesBySearchUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetNowPlayingMoviesUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetPopularMoviesUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetTopRatedMoviesUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetUpcomingMoviesUseCase
import com.tunahankaryagdi.firstproject.ui.base.BaseUiState
import com.tunahankaryagdi.firstproject.ui.base.BaseViewModel
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
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    private val getMoviesBySearchUseCase: GetMoviesBySearchUseCase
) : BaseViewModel<HomeUiState>() {
    override fun createInitialState(): HomeUiState = HomeUiState()

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
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            getTopRatedMoviesUseCase.invoke().cachedIn(viewModelScope).collectLatest { pagingData ->
                _uiState.update { current ->
                    current.copy(
                        movies = pagingData
                    )
                }
            }
        }
        _uiState.value = _uiState.value.copy(isLoading = false)
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

    fun getMoviesBySearch(searchText: String) {
        viewModelScope.launch {
            getMoviesBySearchUseCase.invoke(searchText).cachedIn(viewModelScope).collectLatest { pagingData ->
                _uiState.update { current ->
                    current.copy(
                        searchMovies = pagingData,
                    )
                }
            }
        }
    }
}


data class HomeUiState(
    override val isLoading: Boolean = false,
    val selectedTab: HomeTab = HomeTab.NOW_PLAYING,
    val popularMovies: List<Movie> = emptyList(),
    val movies: PagingData<Movie> = PagingData.empty(),
    val searchMovies: PagingData<SearchMovie> = PagingData.empty()
) : BaseUiState()
