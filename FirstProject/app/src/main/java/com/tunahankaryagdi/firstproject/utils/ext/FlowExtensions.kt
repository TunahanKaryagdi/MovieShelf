package com.tunahankaryagdi.firstproject.utils.ext

import com.tunahankaryagdi.firstproject.domain.error_handling.DataError
import com.tunahankaryagdi.firstproject.domain.error_handling.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

inline fun <T> Flow<Resource<T,DataError>>.collectAndHandle(
    scope: CoroutineScope,
    crossinline onSuccess: (T) -> Unit,
    crossinline onError: (DataError) -> Unit = {}
) {
    scope.launch {
        collect { resource ->
            when (resource) {
                is Resource.Success -> onSuccess(resource.data)
                is Resource.Error -> onError(resource.error)
            }
        }
    }


}