package com.ritesh.snapnews.network

import com.ritesh.snapnews.BuildConfig
import com.ritesh.snapnews.network.dto.NewsResponse
import com.ritesh.snapnews.util.Constants.Companion.PAGE_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface NewsApi {

    @GET("api/1/news")
    suspend fun fetchNews(
        @QueryMap query: Map<String, String>
    ): Response<NewsResponse>
}