package com.tunahankaryagdi.firstproject.domain.use_case

import com.tunahankaryagdi.firstproject.data.model.entity.MovieEntity
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteFavoriteMovieUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    operator fun invoke(movieEntity: MovieEntity): Flow<Boolean> {
        return flow {
            try {
                movieRepository.deleteFavoriteMovie(movieEntity)
                emit(true)
            }
            catch (e: Exception){
                emit(false)
            }
        }
    }

}