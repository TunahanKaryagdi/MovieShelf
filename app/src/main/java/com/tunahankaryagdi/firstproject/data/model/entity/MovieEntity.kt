package com.tunahankaryagdi.firstproject.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tunahankaryagdi.firstproject.domain.model.Movie

@Entity("movies")
data class MovieEntity(
    @PrimaryKey val movieId: Int,
    val title: String,
    val image: String?
)


fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = movieId,
        title = title,
        backdropPath = image
    )
}

