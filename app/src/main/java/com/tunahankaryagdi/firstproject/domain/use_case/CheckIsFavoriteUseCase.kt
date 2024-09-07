package com.tunahankaryagdi.firstproject.domain.use_case

import com.tunahankaryagdi.firstproject.domain.error_handling.DataError
import com.tunahankaryagdi.firstproject.domain.error_handling.Resource
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckIsFavoriteUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    operator fun invoke(movieId: Int): Flow<Resource<Boolean,DataError.Local>> {
        return flow {
            try {
                val response = movieRepository.checkIsFavorite(movieId)
                emit(Resource.Success(response))
            }
            catch (e: Exception){
                emit(Resource.Error(DataError.Local.DISK_FULL))
            }
        }

    }

}