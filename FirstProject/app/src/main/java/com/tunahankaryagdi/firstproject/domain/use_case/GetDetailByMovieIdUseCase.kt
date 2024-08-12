package com.tunahankaryagdi.firstproject.domain.use_case

import coil.network.HttpException
import com.tunahankaryagdi.firstproject.data.model.dto.toMovieDetail
import com.tunahankaryagdi.firstproject.domain.error_handling.DataError
import com.tunahankaryagdi.firstproject.domain.error_handling.Resource
import com.tunahankaryagdi.firstproject.domain.model.MovieDetail
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class GetDetailByMovieIdUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    operator fun invoke(movieId: Int): Flow<Resource<MovieDetail, DataError.Network>> {
        return flow {
            try {
                val movieDto = movieRepository.getDetailByMovieId(movieId)
                emit(Resource.Success(movieDto.toMovieDetail()))
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