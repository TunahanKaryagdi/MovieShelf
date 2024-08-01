package com.tunahankaryagdi.firstproject.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tunahankaryagdi.firstproject.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository,
) : ViewModel() {
    init {
        getPopularMovies()
    }

    fun getPopularMovies() {
        viewModelScope.launch {
            try {
                val response = repository.getPopularMovies()
                println(response.results.size)
            } catch (e: Exception) {
                println("error")
            }
        }
    }
}
