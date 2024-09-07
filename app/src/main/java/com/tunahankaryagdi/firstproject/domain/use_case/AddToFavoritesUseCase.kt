package com.tunahankaryagdi.firstproject.domain.use_case

import com.tunahankaryagdi.firstproject.data.model.entity.MovieEntity
import com.tunahankaryagdi.firstproject.domain.error_handling.DataError
import com.tunahankaryagdi.firstproject.domain.error_handling.Resource
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddToFavoritesUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    operator fun invoke(movieEntity: MovieEntity): Flow<Resource<Boolean,DataError.Local>> {
        return flow {
            try {
                movieRepository.addToFavorites(movieEntity)
                emit(Resource.Success(true))
            }
            catch (e: Exception){
                emit(Resource.Error(DataError.Local.DISK_FULL))
            }
        }
    }

}