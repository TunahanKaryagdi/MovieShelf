package com.tunahankaryagdi.firstproject.data.repository

import com.tunahankaryagdi.firstproject.data.model.ReviewsResponse
import com.tunahankaryagdi.firstproject.data.source.remote.ReviewService
import com.tunahankaryagdi.firstproject.domain.repository.ReviewRepository
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewService: ReviewService
) : ReviewRepository {
    override suspend fun getReviewsByMovieId(movieId: Int): ReviewsResponse {
        return reviewService.getReviewsByMovieId(movieId)
    }

}