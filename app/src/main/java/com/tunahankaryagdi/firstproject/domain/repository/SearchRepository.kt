package com.tunahankaryagdi.firstproject.domain.repository

import androidx.paging.PagingSource
import com.tunahankaryagdi.firstproject.data.model.SearchMoviesResponse
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.model.SearchMovie

interface SearchRepository {
    suspend fun getMoviesBySearch(searchText: String, page: Int = 1): SearchMoviesResponse
    fun getPagingSource(searchText: String): PagingSource<Int, SearchMovie>
}