package com.tunahankaryagdi.firstproject.domain.repository

import androidx.paging.PagingSource
import com.tunahankaryagdi.firstproject.data.model.NowPlayingMoviesResponse
import com.tunahankaryagdi.firstproject.data.model.PopularMoviesResponse
import com.tunahankaryagdi.firstproject.data.model.TopRatedResponse
import com.tunahankaryagdi.firstproject.data.model.UpcomingResponse
import com.tunahankaryagdi.firstproject.data.model.dto.MovieDetailDto
import com.tunahankaryagdi.firstproject.domain.model.Movie
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieRepository {
    suspend fun getPopularMovies(page: Int = 1): PopularMoviesResponse
    suspend fun getNowPlayingMovies(page: Int = 1): NowPlayingMoviesResponse
    suspend fun getTopRatedMovies(page: Int = 1): TopRatedResponse
    suspend fun getUpcomingMovies(page: Int = 1): UpcomingResponse
    suspend fun getDetailByMovieId(movieId: Int): MovieDetailDto
    fun getPagingSource(): PagingSource<Int, Movie>
}

