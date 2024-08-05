package com.tunahankaryagdi.firstproject.data.repository

import androidx.paging.PagingSource
import com.tunahankaryagdi.firstproject.data.model.NowPlayingMoviesResponse
import com.tunahankaryagdi.firstproject.data.model.PopularMoviesResponse
import com.tunahankaryagdi.firstproject.data.model.TopRatedResponse
import com.tunahankaryagdi.firstproject.data.model.UpcomingResponse
import com.tunahankaryagdi.firstproject.data.model.dto.MovieDetailDto
import com.tunahankaryagdi.firstproject.data.source.remote.MovieService
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService,
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int): PopularMoviesResponse {
        return movieService.getPopularMovies(page)
    }

    override suspend fun getNowPlayingMovies(page: Int): NowPlayingMoviesResponse {
        return movieService.getNowPlayingMovies(page)
    }

    override suspend fun getTopRatedMovies(page: Int): TopRatedResponse {
        return movieService.getTopRatedMovies(page)
    }

    override suspend fun getUpcomingMovies(page: Int): UpcomingResponse {
        return movieService.getUpcomingMovies(page)
    }

    override suspend fun getDetailByMovieId(movieId: Int): MovieDetailDto {
        return movieService.getDetailByMovieId(movieId)
    }

    override fun getPagingSource(): PagingSource<Int, Movie> {
        return MoviePagingSource(this)
    }

}
