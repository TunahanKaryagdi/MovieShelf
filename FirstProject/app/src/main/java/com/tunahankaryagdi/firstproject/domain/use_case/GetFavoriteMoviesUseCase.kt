package com.tunahankaryagdi.firstproject.domain.use_case

import com.tunahankaryagdi.firstproject.data.model.entity.toMovie
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFavoriteMoviesUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    operator fun invoke(): Flow<List<Movie>> {
        return flow {
            val movies = movieRepository.getAllFavorites()
            emit(movies.map { it.toMovie() })
        }
    }

}