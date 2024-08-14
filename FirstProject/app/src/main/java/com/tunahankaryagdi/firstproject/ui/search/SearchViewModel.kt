package com.tunahankaryagdi.firstproject.ui.search

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.tunahankaryagdi.firstproject.domain.model.SearchMovie
import com.tunahankaryagdi.firstproject.domain.use_case.GetMoviesBySearchUseCase
import com.tunahankaryagdi.firstproject.ui.base.BaseUiState
import com.tunahankaryagdi.firstproject.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getMoviesBySearchUseCase: GetMoviesBySearchUseCase
) : BaseViewModel<SearchUiState>() {

    override fun createInitialState(): SearchUiState = SearchUiState()

    fun getMoviesBySearch(searchText: String) {
        setState(getCurrentState().copy(isLoading = true))
        viewModelScope.launch {
            getMoviesBySearchUseCase.invoke(searchText).collectLatest { pagingData ->
                setState(getCurrentState().copy(movies = pagingData, isLoading = false))
            }
        }
    }
}

data class SearchUiState(
    val isLoading: Boolean = false,
    val movies: PagingData<SearchMovie> = PagingData.empty()
) : BaseUiState