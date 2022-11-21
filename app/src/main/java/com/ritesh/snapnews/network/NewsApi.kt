package com.ritesh.snapnews.network

import com.ritesh.snapnews.BuildConfig
import com.ritesh.snapnews.network.dto.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun fetchTrendingNews(
        @Query("country") country: String,
        @Query("page") page: Int = 1,
        @Query("category") category: String = "",
        @Query("pageSize") pageSize: Int = 20,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") pageNum: Int = 1
    ): Response<NewsResponse>
}