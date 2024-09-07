package com.tunahankaryagdi.firstproject.data.model.dto

import com.google.gson.annotations.SerializedName
import com.tunahankaryagdi.firstproject.domain.model.Author
import com.tunahankaryagdi.firstproject.domain.model.Review

data class ReviewDto(
    val author: String?,
    @SerializedName("author_details")
    val authorDetails: AuthorDto,
    val content: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    val id: String,
    @SerializedName("updated_at")
    val updatedAt: String?,
    val url: String?
)


data class AuthorDto(
    @SerializedName("avatar_path")
    val avatarPath: String?,
    val name: String?,
    val rating: Any?,
    val username: String?
){
    val numberAsDouble: Double?
        get() = when (rating) {
            is Int -> rating.toDouble()
            is Double -> rating
            else -> null
        }
}

fun ReviewDto.toReview(): Review {
    return Review(
        author = author,
        authorDetails = authorDetails.toAuthor(),
        content = content,
        createdAt = createdAt,
        id = id,
        updatedAt = updatedAt,
        url = url

    )
}

fun AuthorDto.toAuthor(): Author {
    return Author(
        rating = numberAsDouble,
        name = name,
        avatarPath = avatarPath,
        username = username
    )
}