package com.tunahankaryagdi.firstproject.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.model.SearchMovie
import com.tunahankaryagdi.firstproject.domain.use_case.GetMoviesBySearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getMoviesBySearchUseCase: GetMoviesBySearchUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<SearchUiState> = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState>
        get() = _uiState


    fun getMoviesBySearch(searchText: String) {
        viewModelScope.launch {
            getMoviesBySearchUseCase.invoke(searchText).collectLatest { pagingData ->
                _uiState.update { current ->
                    current.copy(
                        movies = pagingData
                    )
                }
            }
        }
    }

}

data class SearchUiState(
    val isLoading: Boolean = false,
    val movies: PagingData<SearchMovie> = PagingData.empty()
)