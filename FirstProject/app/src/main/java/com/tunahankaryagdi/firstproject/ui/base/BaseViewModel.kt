package com.tunahankaryagdi.firstproject.ui.base

import androidx.lifecycle.ViewModel
import com.tunahankaryagdi.firstproject.ui.search.SearchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<T : BaseUiState> : ViewModel() {

    protected val _uiState: MutableStateFlow<T> = MutableStateFlow<T>(createInitialState())
    val uiState: StateFlow<T> get() = _uiState

    protected abstract fun createInitialState(): T

    protected fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.updateLoading(isLoading) as T
    }

    private fun BaseUiState.updateLoading(isLoading: Boolean): BaseUiState {
        return when (this) {
            is SearchUiState -> this.copy(isLoading = isLoading)
            else -> this
        }
    }

}