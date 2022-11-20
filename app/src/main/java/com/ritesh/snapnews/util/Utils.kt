package com.ritesh.snapnews.util

import java.lang.Integer.max
import java.text.SimpleDateFormat
import java.util.*

object Utils {


    fun getRelativeDateTime(dateTime: String): String {
        val date = parseDate(dateTime)
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.timeInMillis = System.currentTimeMillis()
        val seconds = calendar.timeInMillis - date.time
        val relativeTime =
        if(seconds < 60) {
            if (seconds == 1L) "$seconds sec ago" else "$seconds secs ago"
        } else if (seconds < 3600) {
            val min = seconds / 60
            if(min == 1L) "$min min ago" else "$min min ago"
        } else if (seconds < 3600 * 24) {
            val hr = seconds / 3600
            if(hr == 1L) "$hr hr ago" else "$hr hrs ago"
        } else {
            getFormattedDate(date, pattern = "dd EEEE")
        }
        return relativeTime
    }

    fun getDateTime(
        dateTime: String,
    ): String {
        val date = parseDate(dateTime)
        return getFormattedDate(date, pattern = "dd EEEE yyyy, hh:mm aa")
    }

    fun getFormattedDate(date: Date, pattern: String): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }
        return formatter.format(date)
    }

    fun parseDate(date: String): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }
        return formatter.parse(date)!!
    }

    fun calculateReadTime(value: String): String {
        val words = value.split("\\s+".toRegex()).size
        val readingTimeInMinutes = max(words / 150,1)      //150 is the average reading time per minute
        return "$readingTimeInMinutes min read"
    }

    fun getNewsCategory(tabPos: Int): String {
        return when(tabPos) {
            1 -> {
                "business"
            }
            2 -> {
                "entertainment"
            }
            3 -> {
                "general"
            }
            4 -> {
                "health"
            }
            5 -> {
                "science"
            }
            6 -> {
                "sports"
            }
            7 -> {
                "technology"
            }
            else -> {
                ""
            }
        }
    }
}