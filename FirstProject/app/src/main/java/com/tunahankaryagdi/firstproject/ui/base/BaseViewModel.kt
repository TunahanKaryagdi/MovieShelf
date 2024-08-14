package com.tunahankaryagdi.firstproject.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<T : BaseUiState> : ViewModel() {

    private val initialState: T by lazy { createInitialState() }

    private val _uiState: MutableStateFlow<T> = MutableStateFlow(initialState)
    val uiState: StateFlow<T>  = _uiState.asStateFlow()

    protected abstract fun createInitialState(): T

    fun getCurrentState(): T = _uiState.value

    fun setState(state: T) {
        viewModelScope.launch {
            _uiState.emit(state)
        }
    }

}