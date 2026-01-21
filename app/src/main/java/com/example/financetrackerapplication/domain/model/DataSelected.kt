package com.example.financetrackerapplication.domain.model

import android.os.Parcelable
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.data.datasource.local.entity.CategoryEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataSelected<T : Parcelable>(
    val data: T,
    var isSelected: Boolean
) : Parcelable
