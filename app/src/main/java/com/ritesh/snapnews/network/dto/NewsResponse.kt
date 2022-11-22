package com.ritesh.snapnews.network.dto


import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("nextPage")
    val nextPage: Int,
    @SerializedName("results")
    val results: List<Article>,
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int
)