package com.tunahankaryagdi.firstproject.domain.use_case

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tunahankaryagdi.firstproject.data.model.dto.PopularMovieDto
import com.tunahankaryagdi.firstproject.domain.model.PopularMovie
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    operator fun invoke(): Flow<PagingData<PopularMovie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { movieRepository.getPagingSource() }
        ).flow
    }

}
