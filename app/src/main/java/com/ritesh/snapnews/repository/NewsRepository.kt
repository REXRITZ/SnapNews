package com.ritesh.snapnews.repository

import com.ritesh.snapnews.network.NewsApi
import com.ritesh.snapnews.network.dto.NewsResponse
import retrofit2.Response

class NewsRepository(
    private val api: NewsApi
) {

    suspend fun getBreakingNews(
        country: String,
        pageNumber: Int,
        category: String): Response<NewsResponse> {
        return api.fetchTrendingNews(country, pageNumber, category)
    }
}