package com.example.financetrackerapplication.domain.model

import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.iamkamrul.expandablerecyclerviewlist.model.ParentListItem

data class GroupAset(
    val groupName: String,
    val asetList: List<AsetEntity>,
    var isSelected: Boolean
): ParentListItem{
    override fun getChildItemList(): List<*> = asetList
    override fun isInitiallyExpanded(): Boolean = true
}
