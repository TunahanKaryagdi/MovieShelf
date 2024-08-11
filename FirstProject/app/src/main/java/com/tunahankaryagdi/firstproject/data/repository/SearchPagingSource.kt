package com.tunahankaryagdi.firstproject.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tunahankaryagdi.firstproject.data.model.dto.toMovie
import com.tunahankaryagdi.firstproject.data.model.dto.toSearchMovie
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.model.SearchMovie
import com.tunahankaryagdi.firstproject.domain.repository.SearchRepository
import javax.inject.Inject

class SearchPagingSource @Inject constructor(
    private val searchRepository: SearchRepository,
    private val searchText: String
) : PagingSource<Int, SearchMovie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchMovie> {
        return try {
            val page = params.key ?: 1
            val response = searchRepository.getMoviesBySearch(searchText,page)
            LoadResult.Page(
                data = response.results.map { it.toSearchMovie() },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.results.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchMovie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }


}