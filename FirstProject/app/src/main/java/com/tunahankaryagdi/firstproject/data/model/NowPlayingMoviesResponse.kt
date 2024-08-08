package com.tunahankaryagdi.firstproject.data.model

import com.google.gson.annotations.SerializedName
import com.tunahankaryagdi.firstproject.data.model.dto.MovieDto

data class NowPlayingMoviesResponse(
    val dates: Dates,
    val page: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    override val results: List<MovieDto>
) : MovieResponse

data class Dates(
    val maximum: String,
    val minimum: String
)