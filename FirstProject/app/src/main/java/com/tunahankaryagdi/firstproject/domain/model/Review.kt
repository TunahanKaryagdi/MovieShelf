package com.tunahankaryagdi.firstproject.domain.model


data class Review(
    val author: String,
    val authorDetails: Author,
    val content: String,
    val createdAt: String,
    val id: String,
    val updatedAt: String,
    val url: String
)


data class Author(
    val avatarPath: String?,
    val name: String,
    val rating: Int,
    val username: String
)