package com.tunahankaryagdi.firstproject.domain.model

import com.tunahankaryagdi.firstproject.data.model.entity.MovieEntity


data class Movie(
    val id: Int,
    val title: String?,
    val backdropPath: String?,
)


fun Movie.toMovieEntity() : MovieEntity{
    return MovieEntity(
        movieId = id,
        title = title ?: "Default Title",
        image = backdropPath
    )
}