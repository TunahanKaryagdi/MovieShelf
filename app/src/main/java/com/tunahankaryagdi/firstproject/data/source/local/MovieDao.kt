package com.tunahankaryagdi.firstproject.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tunahankaryagdi.firstproject.data.model.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAll(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE movieId = :movieId")
    suspend fun getByID(movieId: Int): MovieEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Delete
    suspend fun delete(movieEntity: MovieEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM movies WHERE movieId = :movieId)")
    suspend fun isFavorite(movieId: Int): Boolean
}