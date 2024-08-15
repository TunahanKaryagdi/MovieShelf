package com.tunahankaryagdi.firstproject.domain.model

data class SearchMovie(
    val id: Int,
    val title: String?,
    val backdropPath: String?,
    val adult: Boolean?,
    val popularity: Double?,
    val releaseDate: String?,
    val voteAverage: Double?
)