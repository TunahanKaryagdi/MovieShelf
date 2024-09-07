package com.tunahankaryagdi.firstproject.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tunahankaryagdi.firstproject.data.model.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}