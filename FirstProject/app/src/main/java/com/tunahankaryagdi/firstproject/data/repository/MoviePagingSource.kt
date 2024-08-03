package com.tunahankaryagdi.firstproject.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tunahankaryagdi.firstproject.data.model.dto.PopularMovieDto
import com.tunahankaryagdi.firstproject.data.model.dto.toPopularMovie
import com.tunahankaryagdi.firstproject.domain.model.PopularMovie
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import javax.inject.Inject

class MoviePagingSource @Inject constructor(private val movieRepository: MovieRepository) :
    PagingSource<Int, PopularMovie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularMovie> {
        return try {
            val page = params.key ?: 1
            val response = movieRepository.getPopularMovies(page)

            LoadResult.Page(
                data = response.results.map { it.toPopularMovie() },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.results.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PopularMovie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }


}