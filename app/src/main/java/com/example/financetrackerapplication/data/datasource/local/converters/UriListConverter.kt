package com.example.financetrackerapplication.data.datasource.local.converters

import android.net.Uri
import androidx.room.TypeConverter

class UriListConverter {
    @TypeConverter
    fun fromUriList(list: List<Uri>?): String?{
        return list?.joinToString(separator = ","){it.toString()}
    }

    @TypeConverter
    fun toUriList(data: String?): List<Uri>?{
        return data?.split(",")?.map { Uri.parse(it) }
    }
}