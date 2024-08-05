package com.tunahankaryagdi.firstproject.data.model

import com.tunahankaryagdi.firstproject.data.model.dto.MovieDto

data class NowPlayingMoviesResponse(
    val dates: Dates,
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)

data class Dates(
    val maximum: String,
    val minimum: String
)