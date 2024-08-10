package com.tunahankaryagdi.firstproject.data.model

import com.google.gson.annotations.SerializedName
import com.tunahankaryagdi.firstproject.data.model.dto.ReviewDto

data class ReviewsResponse(
    val id: Int,
    val page: Int,
    val results: List<ReviewDto>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int,
)


