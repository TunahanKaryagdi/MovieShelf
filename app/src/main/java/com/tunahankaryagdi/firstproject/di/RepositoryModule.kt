package com.tunahankaryagdi.firstproject.di

import com.tunahankaryagdi.firstproject.data.repository.MovieRepositoryImpl
import com.tunahankaryagdi.firstproject.data.repository.ReviewRepositoryImpl
import com.tunahankaryagdi.firstproject.data.repository.SearchRepositoryImpl
import com.tunahankaryagdi.firstproject.data.source.remote.MovieService
import com.tunahankaryagdi.firstproject.domain.repository.MovieRepository
import com.tunahankaryagdi.firstproject.domain.repository.ReviewRepository
import com.tunahankaryagdi.firstproject.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideMovieRepository(movieRepositoryImpl: MovieRepositoryImpl): MovieRepository

    @Binds
    abstract fun provideSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository

    @Binds
    abstract fun provideReviewRepository(reviewRepositoryImpl: ReviewRepositoryImpl): ReviewRepository
}
