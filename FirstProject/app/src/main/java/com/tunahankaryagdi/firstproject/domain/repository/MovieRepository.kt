package com.tunahankaryagdi.firstproject.domain.repository

import com.tunahankaryagdi.firstproject.data.model.PopularMoviesResponse

interface MovieRepository {
    suspend fun getPopularMovies(page: Int = 1): PopularMoviesResponse
}

