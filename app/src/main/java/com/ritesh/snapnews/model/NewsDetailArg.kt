package com.ritesh.snapnews.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class NewsDetailArg(
    val data: List<News>,
    val scrollPosition: Int = 0,
    val searchMode: Boolean,
    val query: String = "",
    val countryCode: String = "",
    val category: String = "",
    val totalResults: Int
) : Parcelable {
}
