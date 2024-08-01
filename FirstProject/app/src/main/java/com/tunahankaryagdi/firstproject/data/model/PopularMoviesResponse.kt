package com.tunahankaryagdi.firstproject.data.model

data class PopularMoviesResponse(
    val page: Int,
    val results: List<PopularMovieDto>,
    val total_pages: Int,
    val total_results: Int,
)

data class PopularMovieDto(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
)
