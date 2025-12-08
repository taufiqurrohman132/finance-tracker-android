package com.example.financetrackerapplication.domain.usecase

import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.domain.model.GroupAset
import com.example.financetrackerapplication.domain.model.ItemAset2
import com.example.financetrackerapplication.domain.model.ItemTransaction
import com.example.financetrackerapplication.domain.repository.AsetRapository
import com.example.financetrackerapplication.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GroupAsetUseCase(
    private val repository: AsetRapository
) {

    operator fun invoke(listAset: List<AsetEntity>, order: List<String>) : Flow<List<GroupAset>>{
        repository.getAset()
            .map { asetList ->
                asetList.groupBy { it.groupAset }
                    .map { (groupName, listAset) ->
                        GroupAset(
                            groupName = groupName,
                            asetList = listAset
                        )
                    }.toList().sortedBy { (key, _) -> order.indexOf(key) }
            }
    }
}