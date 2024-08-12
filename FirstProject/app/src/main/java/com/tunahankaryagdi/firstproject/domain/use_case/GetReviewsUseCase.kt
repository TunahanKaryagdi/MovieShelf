package com.tunahankaryagdi.firstproject.domain.use_case

import coil.network.HttpException
import com.tunahankaryagdi.firstproject.data.model.dto.toMovie
import com.tunahankaryagdi.firstproject.data.model.dto.toReview
import com.tunahankaryagdi.firstproject.domain.error_handling.DataError
import com.tunahankaryagdi.firstproject.domain.error_handling.Resource
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.model.Review
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import com.tunahankaryagdi.firstproject.domain.repository.ReviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetReviewsUseCase @Inject constructor(private val reviewRepository: ReviewRepository) {

    operator fun invoke(movieId: Int): Flow<Resource<List<Review>,DataError.Network>> {
        return flow {
            try {
                val response = reviewRepository.getReviewsByMovieId(movieId).results
                emit(Resource.Success(response.map { it.toReview() }))
            }
            catch (e: HttpException) {
                when (e.response.code) {
                    401 -> emit(Resource.Error(DataError.Network.UNAUTHORIZED))
                    in 500..599 -> emit(Resource.Error(DataError.Network.SERVER_ERROR))
                }
            }
            catch (e: IOException) {
                emit(Resource.Error(DataError.Network.NO_INTERNET))
            }
        }
    }

}
