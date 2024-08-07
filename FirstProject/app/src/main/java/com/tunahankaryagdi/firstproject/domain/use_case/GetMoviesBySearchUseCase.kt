package com.tunahankaryagdi.firstproject.domain.use_case

import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesBySearchUseCase @Inject constructor(private val searchRepository: SearchRepository) {

    operator fun invoke(searctText: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { searchRepository.getPagingSource(searctText) }
        ).flow
    }

}
