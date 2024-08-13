package com.tunahankaryagdi.firstproject.domain.use_case

import com.tunahankaryagdi.firstproject.data.model.entity.toMovie
import com.tunahankaryagdi.firstproject.domain.error_handling.DataError
import com.tunahankaryagdi.firstproject.domain.error_handling.Resource
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFavoriteMoviesUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    suspend operator fun invoke(): Resource<Flow<List<Movie>>,DataError.Local> {
        return try {
            val response = movieRepository.getAllFavorites().map { movieEntities ->
                movieEntities.map { it.toMovie() }
            }
            Resource.Success(response)
        }
        catch (e: Exception){
            Resource.Error(DataError.Local.DISK_FULL)
        }
    }

}