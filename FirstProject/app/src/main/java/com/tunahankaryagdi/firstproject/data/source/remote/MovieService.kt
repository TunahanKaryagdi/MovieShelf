package com.tunahankaryagdi.firstproject.data.source.remote

import com.tunahankaryagdi.firstproject.data.model.PopularMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") page: Int = 1): PopularMoviesResponse
}
