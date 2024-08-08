package com.tunahankaryagdi.firstproject.data.model

import com.tunahankaryagdi.firstproject.data.model.dto.MovieDto

interface MovieResponse {
    val results: List<MovieDto>
}