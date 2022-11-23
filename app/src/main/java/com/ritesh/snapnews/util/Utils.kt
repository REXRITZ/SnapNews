package com.ritesh.snapnews.util

import java.lang.Integer.max
import java.text.SimpleDateFormat
import java.util.*

object Utils {


    fun getRelativeDateTime(dateTime: String): String {
        val date = parseDate(dateTime)
        val seconds = (System.currentTimeMillis() - date.time) / 1000
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

    private fun getFormattedDate(date: Date, pattern: String): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }
        return formatter.format(date)
    }

    private fun parseDate(date: String): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }
        return formatter.parse(date)!!
    }

    fun calculateReadTime(value: String): String {
        val words = value.split("\\s+".toRegex()).size
        // 150 is taken as the average reading time per minute
        val readingTimeInMinutes = max(words / 150,1)
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
                "environment"
            }
            4 -> {
                "food"
            }
            5 -> {
                "health"
            }
            6 -> {
                "politics"
            }
            7 -> {
                "science"
            }
            8 -> {
                "sports"
            }
            9 -> {
                "technology"
            }
            10 -> {
                "top"
            }
            11 -> {
                "world"
            }
            else -> {
                ""
            }
        }
    }
}