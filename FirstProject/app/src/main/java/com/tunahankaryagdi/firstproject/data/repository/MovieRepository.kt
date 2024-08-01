package com.tunahankaryagdi.firstproject.data.repository

import com.tunahankaryagdi.firstproject.data.model.PopularMoviesResponse
import com.tunahankaryagdi.firstproject.data.source.remote.MovieService
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieService: MovieService,
) {
    suspend fun getPopularMovies(): PopularMoviesResponse = movieService.getPopularMovies()
}
