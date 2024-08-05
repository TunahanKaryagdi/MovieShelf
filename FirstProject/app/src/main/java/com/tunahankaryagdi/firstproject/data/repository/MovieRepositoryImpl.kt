package com.tunahankaryagdi.firstproject.data.repository

import androidx.paging.PagingSource
import com.tunahankaryagdi.firstproject.data.model.PopularMoviesResponse
import com.tunahankaryagdi.firstproject.data.model.dto.MovieDetailDto
import com.tunahankaryagdi.firstproject.data.model.dto.PopularMovieDto
import com.tunahankaryagdi.firstproject.data.source.remote.MovieService
import com.tunahankaryagdi.firstproject.domain.model.PopularMovie
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService,
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int): PopularMoviesResponse {
        return movieService.getPopularMovies(page)
    }

    override suspend fun getDetailByMovieId(movieId: Int): MovieDetailDto {
        return movieService.getDetailByMovieId(movieId)
    }

    override fun getPagingSource(): PagingSource<Int, PopularMovie> {
        return MoviePagingSource(this)
    }

}
