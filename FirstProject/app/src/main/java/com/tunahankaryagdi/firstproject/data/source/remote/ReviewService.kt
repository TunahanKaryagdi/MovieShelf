package com.tunahankaryagdi.firstproject.data.source.remote

import com.tunahankaryagdi.firstproject.data.model.ReviewsResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface ReviewService {
    @GET("movie/{movieId}/reviews")
    suspend fun getReviewsByMovieId(@Path("movieId") movieId: Int) : ReviewsResponse
}