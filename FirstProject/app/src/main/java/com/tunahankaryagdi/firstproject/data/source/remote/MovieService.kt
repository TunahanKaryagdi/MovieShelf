package com.tunahankaryagdi.firstproject.data.source.remote

import com.tunahankaryagdi.firstproject.data.model.PopularMoviesResponse
import com.tunahankaryagdi.firstproject.data.model.dto.MovieDetailDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int = 1): PopularMoviesResponse

    @GET("movie/{movieId}")
    suspend fun getDetailByMovieId(@Path("movieId") movieId: Int): MovieDetailDto
}

