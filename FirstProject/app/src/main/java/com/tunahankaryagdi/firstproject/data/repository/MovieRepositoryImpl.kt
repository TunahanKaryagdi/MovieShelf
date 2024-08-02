package com.tunahankaryagdi.firstproject.data.repository

import com.tunahankaryagdi.firstproject.data.model.PopularMoviesResponse
import com.tunahankaryagdi.firstproject.data.source.remote.MovieService
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService,
) : MovieRepository {
    override suspend fun getPopularMovies(page: Int): PopularMoviesResponse {
        return movieService.getPopularMovies(page)
    }

}
