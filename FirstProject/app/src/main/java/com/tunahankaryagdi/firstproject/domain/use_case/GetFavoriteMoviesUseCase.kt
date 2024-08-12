package com.tunahankaryagdi.firstproject.domain.use_case

import com.tunahankaryagdi.firstproject.data.model.entity.toMovie
import com.tunahankaryagdi.firstproject.domain.error_handling.DataError
import com.tunahankaryagdi.firstproject.domain.error_handling.Resource
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFavoriteMoviesUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    operator fun invoke(): Flow<Resource<List<Movie>,DataError.Local>> {
        return flow {
            try {
                val movies = movieRepository.getAllFavorites()
                emit(Resource.Success(movies.map { it.toMovie() }))
            }
            catch (e: Exception){
                emit(Resource.Error(DataError.Local.DISK_FULL))
            }
        }
    }

}