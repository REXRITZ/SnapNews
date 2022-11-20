package com.ritesh.snapnews.di

import com.ritesh.snapnews.network.NewsApi
import com.ritesh.snapnews.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideApiInstance() = Retrofit.Builder()
        .baseUrl("https://newsapi.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NewsApi::class.java)

    @Provides
    @Singleton
    fun provideNewsRepository(
        api: NewsApi
    ) = NewsRepository(api)
}