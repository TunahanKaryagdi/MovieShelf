package com.tunahankaryagdi.firstproject.data.repository

import androidx.paging.PagingSource
import com.tunahankaryagdi.firstproject.data.model.SearchMoviesResponse
import com.tunahankaryagdi.firstproject.data.source.remote.SearchService
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchService: SearchService
) : SearchRepository {
    override suspend fun getMoviesBySearch(searchText: String, page: Int): SearchMoviesResponse {
        return searchService.getMoviesBySearch(searchText, page)
    }

    override fun getPagingSource(searchText: String): PagingSource<Int, Movie> {
        return SearchPagingSource(this,searchText)
    }

}