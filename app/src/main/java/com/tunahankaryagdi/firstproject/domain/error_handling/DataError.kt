package com.tunahankaryagdi.firstproject.domain.error_handling


sealed interface DataError : Error {
    enum class Network : DataError {
        NO_INTERNET,
        UNAUTHORIZED,
        SERVER_ERROR,
    }

    enum class Local : DataError {
        DISK_FULL
    }
}