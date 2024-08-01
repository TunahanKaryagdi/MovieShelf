package com.tunahankaryagdi.firstproject.di

import com.tunahankaryagdi.firstproject.data.repository.MovieRepository
import com.tunahankaryagdi.firstproject.data.source.remote.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    fun provideMovieRepository(movieService: MovieService): MovieRepository = MovieRepository(movieService)
}
