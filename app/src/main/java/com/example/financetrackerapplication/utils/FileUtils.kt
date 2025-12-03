package com.example.financetrackerapplication.utils

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileUtils {
    private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
    private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

    fun createCustomTempFile(context: Context) : File{
        val fileDir = context.externalCacheDir
        return File.createTempFile(timeStamp, ".jpg", fileDir)
    }
}