package com.tunahankaryagdi.firstproject.domain.model

import com.tunahankaryagdi.firstproject.data.model.entity.MovieEntity


data class MovieDetail(
    val id: Int,
    val adult: Boolean?,
    val backdropPath: String?,
    val collectionInfo: CollectionInfo?,
    val originalTitle: String?,
    val overview: String?,
    val posterPath: String?,
    val releaseDate: String?,
    val voteAverage: Double?,
    val genres: List<Genre>?,
    val runtime: Int?
)

data class Genre(
    val id: Int,
    val name: String?
)

data class CollectionInfo(
    val id: Int,
    val name: String?,
    val posterPath: String?,
    val backdropPath: String?
)

fun MovieDetail.toMovieEntity(): MovieEntity{
    return MovieEntity(
        movieId = id,
        title = originalTitle ?: "Default title",
        image = backdropPath
    )
}