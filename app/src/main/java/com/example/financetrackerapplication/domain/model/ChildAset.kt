package com.example.financetrackerapplication.domain.model

import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity

data class ChildAset(
    val aset: AsetEntity,
    var isSelected: Boolean
)
