package com.example.financetrackerapplication.utils

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object TimeUtils {
    fun getDayName(dateStr: String): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse(dateStr, formatter)
        return date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("id"))
    }

    fun getDate(dateTimeMillis: Long): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())
        return Instant.ofEpochMilli(dateTimeMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(formatter)
    }

    fun getStyleDate(dateStr: String): CharSequence {
        val parts = dateStr.split("-")
        val day = parts[0]
        val monthYear = parts.subList(1, parts.size).joinToString("-")

        val builder = SpannableStringBuilder()
        builder.apply {
            append(day)
            setSpan(StyleSpan(Typeface.BOLD), 0, day.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(
                RelativeSizeSpan(1.3f),// 1.5x dari ukuran normal
                0,
                day.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            builder.append("-$monthYear")
        }
        return builder
    }
}