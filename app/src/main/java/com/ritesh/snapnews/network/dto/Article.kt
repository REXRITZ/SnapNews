package com.ritesh.snapnews.network.dto


import com.google.gson.annotations.SerializedName
import com.ritesh.snapnews.model.News
import com.ritesh.snapnews.util.Utils

data class Article(
    @SerializedName("category")
    val category: List<String>,
    @SerializedName("content")
    val content: String?,
    @SerializedName("country")
    val country: List<String>,
    @SerializedName("creator")
    val creator: List<String>?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("keywords")
    val keywords: List<String>?,
    @SerializedName("language")
    val language: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("pubDate")
    val pubDate: String,
    @SerializedName("source_id")
    val sourceId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("video_url")
    val videoUrl: Any?
) {
    fun toNews(): News {
        return News(
            title = title,
            author = creator?.get(0) ?: "",
            description = description,
            publishAt = pubDate,
            imageUrl = imageUrl,
            newsUrl = link,
            readTime = Utils.calculateReadTime(description + content + "")
        )
    }
}