package com.tunahankaryagdi.firstproject.domain.repository

import com.tunahankaryagdi.firstproject.data.model.ReviewsResponse


interface ReviewRepository {
    suspend fun getReviewsByMovieId(movieId: Int): ReviewsResponse
}