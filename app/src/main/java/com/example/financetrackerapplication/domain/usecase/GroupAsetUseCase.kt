package com.example.financetrackerapplication.domain.usecase

import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.domain.model.ChildAset
import com.example.financetrackerapplication.domain.model.GroupAset
import com.example.financetrackerapplication.domain.model.ItemTransaction
import com.example.financetrackerapplication.domain.repository.AsetRapository
import com.example.financetrackerapplication.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GroupAsetUseCase(
    private val repository: AsetRapository
) {
    operator fun invoke(
        order: List<String>
    ): Flow<List<GroupAset.Parent>> {

        return repository.getAset()
            .map { asetList ->
                asetList
                    .groupBy { it.groupAset } // String
                    .toList()
                    .sortedBy { (key, _) ->
                        val index = order.indexOf(key)
                        if (index == -1) Int.MAX_VALUE else index
                    }
                    .map { (groupName, listAset) ->

                        GroupAset.Parent(
                            id = groupName,
                            name = groupName,
                            childAsetList = listAset.map { aset ->
                                GroupAset.Child(
                                    aset = aset,
                                    isSelected = false
                                )
                            },
                            isExpanded = true,
                            isSelected = false
                        )
                    }
            }
    }

}