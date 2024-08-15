package com.tunahankaryagdi.firstproject.ui.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.model.SearchMovie
import com.tunahankaryagdi.firstproject.domain.use_case.GetMoviesBySearchUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetNowPlayingMoviesUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetPopularMoviesUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetTopRatedMoviesUseCase
import com.tunahankaryagdi.firstproject.domain.use_case.GetUpcomingMoviesUseCase
import com.tunahankaryagdi.firstproject.ui.base.BaseUiState
import com.tunahankaryagdi.firstproject.ui.base.BaseViewModel
import com.tunahankaryagdi.firstproject.utils.enums.HomeTab
import com.tunahankaryagdi.firstproject.utils.ext.collectAndHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
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
        init()
    }

    fun init(){
        getPopularMovies()
        getMovies()
    }

    fun setTab(tab: HomeTab) {
        val state = getCurrentState()
        if (tab == state.selectedTab) return
        setState(getCurrentState().copy(selectedTab = tab))
        getMovies()
    }

    private fun getPopularMovies() {
        setState(getCurrentState().copy(isLoading = true))
        viewModelScope.launch {
            getPopularMoviesUseCase.invoke().collectAndHandle(
                scope = this,
                onSuccess = { data ->
                    setState(getCurrentState().copy(popularMovies = data, isLoading = false))
                },
                onError = {

                }
            )
        }
    }

    private fun getNowPlayingMovies() {
        viewModelScope.launch {
            setState(getCurrentState().copy(isLoading = true))
            getNowPlayingMoviesUseCase.invoke().cachedIn(viewModelScope)
                .collect { pagingData ->
                    setState(getCurrentState().copy(isLoading = false, movies = pagingData))
                }
        }
    }

    private fun getTopRatedMovies() {
        viewModelScope.launch {
            getTopRatedMoviesUseCase.invoke().cachedIn(viewModelScope).collectLatest { pagingData ->
                setState(getCurrentState().copy(movies = pagingData))
            }
        }
    }

    private fun getUpcomingMovies() {
        viewModelScope.launch {
            getUpcomingMoviesUseCase.invoke().cachedIn(viewModelScope).collectLatest { pagingData ->
                setState(getCurrentState().copy(movies = pagingData))
            }
        }
    }


    private fun getMovies() {
        when (getCurrentState().selectedTab) {
            HomeTab.NOW_PLAYING -> getNowPlayingMovies()
            HomeTab.TOP_RATED -> getTopRatedMovies()
            HomeTab.UPCOMING -> getUpcomingMovies()
        }
    }

    fun getMoviesBySearch(searchText: String) {
        viewModelScope.launch {
            getMoviesBySearchUseCase.invoke(searchText).cachedIn(viewModelScope).collectLatest { pagingData ->
                setState(getCurrentState().copy(searchMovies = pagingData))
            }
        }
    }
}


data class HomeUiState(
    val isLoading: Boolean = false,
    val selectedTab: HomeTab = HomeTab.NOW_PLAYING,
    val popularMovies: List<Movie> = emptyList(),
    val movies: PagingData<Movie> = PagingData.empty(),
    val searchMovies: PagingData<SearchMovie> = PagingData.empty()
) : BaseUiState
