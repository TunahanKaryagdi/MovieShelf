package com.tunahankaryagdi.firstproject.di

import android.content.Context
import androidx.room.Room
import com.tunahankaryagdi.firstproject.BuildConfig
import com.tunahankaryagdi.firstproject.data.source.local.AppDatabase
import com.tunahankaryagdi.firstproject.data.source.local.MovieDao
import com.tunahankaryagdi.firstproject.data.source.remote.interceptor.HeaderInterceptor
import com.tunahankaryagdi.firstproject.data.source.remote.interceptor.NetworkInterceptor
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
    fun provideOkHttpClient(headerInterceptor: HeaderInterceptor, networkInterceptor: NetworkInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(networkInterceptor)
            .build()

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
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

    @Provides
    fun provideNetworkInterceptor(@ApplicationContext context: Context): NetworkInterceptor =
        NetworkInterceptor(context)
}
