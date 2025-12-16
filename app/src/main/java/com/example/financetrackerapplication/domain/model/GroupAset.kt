package com.example.financetrackerapplication.domain.model

import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.domain.usecase.GroupAsetUseCase
import com.iamkamrul.expandablerecyclerviewlist.model.ParentListItem

sealed class GroupAset{
    data class Parent(
        val id: String,
        val name: String,
        val childAsetList: List<Child>,
        val isExpanded: Boolean = true,
        val isSelected: Boolean = false
    ): GroupAset()

    data class Child(
        val aset: AsetEntity,
        var isSelected: Boolean
    ): GroupAset()
}
