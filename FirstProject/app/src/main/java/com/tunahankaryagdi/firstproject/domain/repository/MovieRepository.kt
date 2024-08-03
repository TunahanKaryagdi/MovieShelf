package com.tunahankaryagdi.firstproject.domain.repository

import androidx.paging.PagingSource
import com.tunahankaryagdi.firstproject.data.model.PopularMoviesResponse
import com.tunahankaryagdi.firstproject.domain.model.PopularMovie

interface MovieRepository {
    suspend fun getPopularMovies(page: Int = 1): PopularMoviesResponse
    fun getPagingSource(): PagingSource<Int, PopularMovie>
}

