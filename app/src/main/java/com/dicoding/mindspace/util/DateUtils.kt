package com.dicoding.mindspace.util

import java.text.SimpleDateFormat
import java.util.Locale

fun formatDate(timestamp: String): String {
    val dateFormatter =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS ZZZZ", Locale.getDefault())
    val displayFormat = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault())

    val date = dateFormatter.parse(timestamp)
    return displayFormat.format(date!!)
}