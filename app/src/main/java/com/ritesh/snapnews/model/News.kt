package com.ritesh.snapnews.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    val title: String,
    val author: String,
    val description: String?,
    val publishAt: String,
    val imageUrl: String?,
    val newsUrl: String,
    val readTime: String
) : Parcelable