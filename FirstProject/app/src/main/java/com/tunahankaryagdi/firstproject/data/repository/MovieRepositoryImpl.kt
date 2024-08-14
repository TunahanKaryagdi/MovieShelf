package com.tunahankaryagdi.firstproject.data.repository

import androidx.paging.PagingSource
import com.tunahankaryagdi.firstproject.data.model.NowPlayingMoviesResponse
import com.tunahankaryagdi.firstproject.data.model.PopularMoviesResponse
import com.tunahankaryagdi.firstproject.data.model.SimilarMoviesResponse
import com.tunahankaryagdi.firstproject.data.model.TopRatedResponse
import com.tunahankaryagdi.firstproject.data.model.UpcomingResponse
import com.tunahankaryagdi.firstproject.data.model.dto.MovieDetailDto
import com.tunahankaryagdi.firstproject.data.model.entity.MovieEntity
import com.tunahankaryagdi.firstproject.data.source.local.MovieDao
import com.tunahankaryagdi.firstproject.data.source.remote.MovieService
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import com.tunahankaryagdi.firstproject.utils.enums.MovieType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService,
    private val movieDao: MovieDao
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

    override suspend fun getSimilarMoviesByMovieId(movieId: Int): SimilarMoviesResponse {
        return movieService.getSimilarMoviesByMovieId(movieId)
    }

    override suspend fun addToFavorites(movieEntity: MovieEntity) {
        return movieDao.insert(movieEntity)
    }

    override suspend fun getAllFavorites(): Flow<List<MovieEntity>> {
        return movieDao.getAll()
    }

    override suspend fun deleteFavoriteMovie(movieEntity: MovieEntity) {
        return movieDao.delete(movieEntity)
    }

    override suspend fun checkIsFavorite(movieId: Int): Boolean {
        return movieDao.isFavorite(movieId)
    }

    override fun getPagingSource(movieType: MovieType): PagingSource<Int, Movie> {
        return MoviePagingSource(this, movieType)
    }

}
