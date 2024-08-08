package com.tunahankaryagdi.firstproject.data.model

import com.tunahankaryagdi.firstproject.data.model.dto.MovieDto

data class TopRatedResponse(
    val page: Int,
    override val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
) : MovieResponse