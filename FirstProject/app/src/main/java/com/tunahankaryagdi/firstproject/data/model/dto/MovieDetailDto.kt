package com.tunahankaryagdi.firstproject.data.model.dto

import com.google.gson.annotations.SerializedName
import com.tunahankaryagdi.firstproject.domain.model.CollectionInfo
import com.tunahankaryagdi.firstproject.domain.model.Genre
import com.tunahankaryagdi.firstproject.domain.model.MovieDetail

data class MovieDetailDto(
    val id: Int,
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("belongs_to_collection")
    val collectionInfo: CollectionInfoDto?,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    val genres: List<GenreDto>,
    val runtime: Int
)


data class CollectionInfoDto(
    val id: Int,
    val name: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
)

data class GenreDto(
    val id: Int,
    val name: String
)


fun MovieDetailDto.toMovieDetail(): MovieDetail {
    return MovieDetail(
        id = id,
        adult = adult,
        posterPath = posterPath,
        backdropPath = backdropPath,
        overview = overview,
        originalTitle = originalTitle,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        runtime = runtime,
        collectionInfo = collectionInfo?.toCollectionInfo(),
        genres = genres.map { it.toGenre() },
    )
}

fun CollectionInfoDto.toCollectionInfo(): CollectionInfo {
    return CollectionInfo(
        id = id,
        name = name,
        posterPath = posterPath,
        backdropPath = backdropPath
    )
}

fun GenreDto.toGenre(): Genre {
    return Genre(
        id = id,
        name = name
    )
}