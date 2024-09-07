package com.tunahankaryagdi.firstproject.data.model

import com.google.gson.annotations.SerializedName
import com.tunahankaryagdi.firstproject.data.model.dto.MovieDto

data class SearchMoviesResponse(
    val page: Int,
    override val results: List<MovieDto>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int,
) : MovieResponse