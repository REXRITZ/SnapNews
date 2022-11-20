package com.ritesh.snapnews.network.dto


import com.google.gson.annotations.SerializedName
import com.ritesh.snapnews.model.News
import com.ritesh.snapnews.util.Utils.calculateReadTime

data class Article(
    @SerializedName("author")
    val author: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName("source")
    val source: Source,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("urlToImage")
    val urlToImage: String?
) {
    fun toNews() = News(
        title = title,
        description = description,
        publishAt = publishedAt,
        author = author,
        newsUrl = url,
        imageUrl = urlToImage,
        readTime = calculateReadTime(title + description + content + "")
    )
}