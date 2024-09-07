package com.tunahankaryagdi.firstproject.domain.repository

import androidx.paging.PagingSource
import com.tunahankaryagdi.firstproject.data.model.NowPlayingMoviesResponse
import com.tunahankaryagdi.firstproject.data.model.PopularMoviesResponse
import com.tunahankaryagdi.firstproject.data.model.SimilarMoviesResponse
import com.tunahankaryagdi.firstproject.data.model.TopRatedResponse
import com.tunahankaryagdi.firstproject.data.model.UpcomingResponse
import com.tunahankaryagdi.firstproject.data.model.dto.MovieDetailDto
import com.tunahankaryagdi.firstproject.data.model.entity.MovieEntity
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.utils.enums.MovieType
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(page: Int = 1): PopularMoviesResponse
    suspend fun getNowPlayingMovies(page: Int = 1): NowPlayingMoviesResponse
    suspend fun getTopRatedMovies(page: Int = 1): TopRatedResponse
    suspend fun getUpcomingMovies(page: Int = 1): UpcomingResponse
    suspend fun getDetailByMovieId(movieId: Int): MovieDetailDto
    suspend fun getSimilarMoviesByMovieId(movieId: Int): SimilarMoviesResponse
    suspend fun addToFavorites(movieEntity: MovieEntity)
    suspend fun getAllFavorites(): Flow< List<MovieEntity>>
    suspend fun deleteFavoriteMovie(movieEntity: MovieEntity)
    suspend fun checkIsFavorite(movieId: Int) : Boolean
    fun getPagingSource(movieType: MovieType): PagingSource<Int, Movie>
}

