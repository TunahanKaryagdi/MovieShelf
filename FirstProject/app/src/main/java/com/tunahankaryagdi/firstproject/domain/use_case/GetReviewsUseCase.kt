package com.tunahankaryagdi.firstproject.domain.use_case

import com.tunahankaryagdi.firstproject.data.model.dto.toMovie
import com.tunahankaryagdi.firstproject.data.model.dto.toReview
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.model.Review
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import com.tunahankaryagdi.firstproject.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetReviewsUseCase @Inject constructor(private val reviewRepository: ReviewRepository) {

    operator fun invoke(movieId: Int): Flow<List<Review>> {
        return flow {
            val response = reviewRepository.getReviewsByMovieId(movieId).results
            emit(response.map { it.toReview() })
        }
    }

}
