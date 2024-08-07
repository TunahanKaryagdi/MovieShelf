package com.tunahankaryagdi.firstproject.di

import android.content.Context
import androidx.room.Room
import com.tunahankaryagdi.firstproject.data.source.local.AppDatabase
import com.tunahankaryagdi.firstproject.data.source.local.MovieDao
import com.tunahankaryagdi.firstproject.data.source.remote.interceptor.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideOkHttpClient(headerInterceptor: HeaderInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(headerInterceptor)
            .build()

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "Movies"
        ).fallbackToDestructiveMigration().build()


    @Provides
    fun provideMovieDao(appDatabase: AppDatabase): MovieDao = appDatabase.movieDao()
}
