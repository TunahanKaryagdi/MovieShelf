package com.tunahankaryagdi.firstproject.ui.home_search

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tunahankaryagdi.firstproject.domain.model.SearchMovie
import com.tunahankaryagdi.firstproject.domain.use_case.GetMoviesBySearchUseCase
import com.tunahankaryagdi.firstproject.ui.base.BaseUiState
import com.tunahankaryagdi.firstproject.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeSearchViewModel @Inject constructor(
    private val getMoviesBySearchUseCase: GetMoviesBySearchUseCase
) : BaseViewModel<HomeSearchUiState>(){
    override fun createInitialState(): HomeSearchUiState = HomeSearchUiState()



    fun getMoviesBySearch(searchText: String) {
        setState(getCurrentState().copy(true))
        viewModelScope.launch {
            getMoviesBySearchUseCase.invoke(searchText).cachedIn(viewModelScope).collectLatest { pagingData ->
                setState(getCurrentState().copy(movies = pagingData, isLoading = false))
            }
        }
    }
}

data class HomeSearchUiState(
    val isLoading: Boolean = false,
    val movies: PagingData<SearchMovie> = PagingData.empty()
) : BaseUiState