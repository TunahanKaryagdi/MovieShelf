package com.tunahankaryagdi.firstproject.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tunahankaryagdi.firstproject.data.model.dto.toMovie
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import com.tunahankaryagdi.firstproject.utils.enums.MovieType
import javax.inject.Inject

class MoviePagingSource @Inject constructor(
    private val movieRepository: MovieRepository,
    private val movieType: MovieType
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1

            val response = when (movieType) {
                MovieType.POPULAR -> movieRepository.getPopularMovies(page)
                MovieType.TOP_RATED -> movieRepository.getTopRatedMovies(page)
                MovieType.UPCOMING -> movieRepository.getUpcomingMovies(page)
                MovieType.NOW_PLAYING -> movieRepository.getNowPlayingMovies(page)
            }


            LoadResult.Page(
                data = response.results.map { it.toMovie() },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.results.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }


}