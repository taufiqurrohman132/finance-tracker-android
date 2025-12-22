package com.example.financetrackerapplication.utils

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.example.financetrackerapplication.R
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

object TimeUtils {
    fun getDayName(dateStr: String): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse(dateStr, formatter)
        return date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    }

    fun getDate(dateTimeMillis: Long): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())
        return Instant.ofEpochMilli(dateTimeMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(formatter)
    }

    fun getTime(dateTimeMillis: Long): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
        return Instant.ofEpochMilli(dateTimeMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
            .format(formatter)
    }

    fun combineDateTimeMillis(dateStr: String, time: String): Long {
        return try {
            val localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            val localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
            val localDateTime = LocalDateTime.of(localDate, localTime)
            localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
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

    //    Hitung jumlah hari dalam bulan
    fun daysInMonth(year: Int, month: Int): Int {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    //    Ambil hari dari datetimeMillis
    fun getDayOfMonth(timeMillis: Long): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timeMillis
        return cal.get(Calendar.DAY_OF_MONTH)
    }

    private fun parseMonthFromRes(context: Context, monthText: String): Month{
        val month = context.resources.getStringArray(R.array.month_short_id)

        val index = month.indexOf(monthText)
        if (index == -1) error("Bulan Tidak Valid")

        return Month.of(index +1)
    }

    fun parseMonthYear(
        context: Context,
        text: String
    ): Pair<Int, Month>{
        val parts = text.trim().split(" ")
        val month = parseMonthFromRes(context, parts[0])
        val year = parts[1].toInt()

        return year to month
    }

}