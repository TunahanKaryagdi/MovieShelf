package com.tunahankaryagdi.firstproject.domain.repository

import androidx.paging.PagingSource
import com.tunahankaryagdi.firstproject.data.model.PopularMoviesResponse
import com.tunahankaryagdi.firstproject.data.model.dto.MovieDetailDto
import com.tunahankaryagdi.firstproject.domain.model.PopularMovie

interface MovieRepository {
    suspend fun getPopularMovies(page: Int = 1): PopularMoviesResponse
    suspend fun getDetailByMovieId(movieId: Int): MovieDetailDto
    fun getPagingSource(): PagingSource<Int, PopularMovie>
}

