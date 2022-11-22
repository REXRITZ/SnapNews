package com.ritesh.snapnews.repository

import com.ritesh.snapnews.BuildConfig
import com.ritesh.snapnews.network.NewsApi
import com.ritesh.snapnews.network.dto.NewsResponse
import retrofit2.Response

class NewsRepository(
    private val api: NewsApi
) {

    suspend fun getBreakingNews(
        countryCode: String = "",
        pageNumber: Int = 1,
        category: String = "",
        searchText: String = ""
    ): Response<NewsResponse> {
        val queryMap = mutableMapOf<String,String>()
        queryMap["apikey"] = BuildConfig.API_KEY
        queryMap["page"] = pageNumber.toString()
        if (countryCode.isNotEmpty())
            queryMap["country"] = countryCode
        if (category.isNotEmpty())
            queryMap["category"] = category
        if(searchText.isNotEmpty())
            queryMap["q"] = searchText

        return api.fetchNews(queryMap)
    }

}