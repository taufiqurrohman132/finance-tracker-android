package com.example.financetrackerapplication.domain.model

import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.iamkamrul.expandablerecyclerviewlist.model.ParentListItem

data class ChildAset(
    val aset: AsetEntity,
    var isSelected: Boolean
)
