package com.tunahankaryagdi.firstproject.domain.use_case

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tunahankaryagdi.firstproject.domain.model.SearchMovie
import com.tunahankaryagdi.firstproject.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesBySearchUseCase @Inject constructor(private val searchRepository: SearchRepository) {

    operator fun invoke(searctText: String): Flow<PagingData<SearchMovie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { searchRepository.getPagingSource(searctText) }
        ).flow
    }

}
