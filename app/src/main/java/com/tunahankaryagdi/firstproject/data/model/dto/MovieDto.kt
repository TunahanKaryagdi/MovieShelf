package com.tunahankaryagdi.firstproject.data.model.dto

import com.google.gson.annotations.SerializedName
import com.tunahankaryagdi.firstproject.domain.model.Movie
import com.tunahankaryagdi.firstproject.domain.model.SearchMovie

data class MovieDto(
    val adult: Boolean?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Int>?,
    val id: Int,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("original_title")
    val originalTitle: String?,
    val overview: String?,
    val popularity: Double?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    val title: String?,
    val video: Boolean?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("vote_count")
    val voteCount: Int?,
)


fun MovieDto.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        backdropPath = backdropPath
    )
}

fun MovieDto.toSearchMovie(): SearchMovie {
    return SearchMovie(
        id = id,
        title = title,
        voteAverage = voteAverage,
        adult = adult,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        popularity = popularity
    )
}
