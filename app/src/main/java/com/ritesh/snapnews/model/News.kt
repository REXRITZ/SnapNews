package com.ritesh.snapnews.model

data class News(
    val title: String,
    val author: String?,
    val description: String?,
    val publishAt: String,
    val imageUrl: String?,
    val newsUrl: String,
    val readTime: String
)