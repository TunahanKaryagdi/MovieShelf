package com.tunahankaryagdi.firstproject.data.model

import com.google.gson.annotations.SerializedName
import com.tunahankaryagdi.firstproject.data.model.dto.MovieDto

data class SimilarMoviesResponse(
    val page: Int,
    val results: List<MovieDto>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)