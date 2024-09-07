package com.tunahankaryagdi.firstproject.domain.use_case

import coil.network.HttpException
import com.tunahankaryagdi.firstproject.data.model.dto.toMovie
import com.tunahankaryagdi.firstproject.data.model.dto.toMovieDetail
import com.tunahankaryagdi.firstproject.domain.error_handling.DataError
import com.tunahankaryagdi.firstproject.domain.error_handling.Resource
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    operator fun invoke(): Flow<Resource<List<Movie>, DataError.Network>> {
        return flow {
            try {
                val movieDtos = movieRepository.getPopularMovies().results
                emit(Resource.Success(movieDtos.map { it.toMovie() }))
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


