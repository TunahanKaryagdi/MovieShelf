package com.tunahankaryagdi.firstproject.data.source.remote

import com.tunahankaryagdi.firstproject.data.model.SearchMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("search/movie")
    suspend fun getMoviesBySearch(@Query("query") searchText: String, @Query("page") page: Int = 1): SearchMoviesResponse
}