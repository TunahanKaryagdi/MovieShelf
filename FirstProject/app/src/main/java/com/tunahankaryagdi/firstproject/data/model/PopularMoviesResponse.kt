package com.tunahankaryagdi.firstproject.data.model

import com.google.gson.annotations.SerializedName
import com.tunahankaryagdi.firstproject.data.model.dto.PopularMovieDto

data class PopularMoviesResponse(
    val page: Int,
    val results: List<PopularMovieDto>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int,
)

