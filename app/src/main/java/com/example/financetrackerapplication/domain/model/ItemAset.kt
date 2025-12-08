package com.example.financetrackerapplication.domain.model

import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.iamkamrul.expandablerecyclerviewlist.model.ParentListItem

data class ItemAset(
    val subName: String,
    val asetList: List<AsetEntity>
): ParentListItem{
    override fun getChildItemList(): List<*> = asetList
    override fun isInitiallyExpanded(): Boolean = false
}
