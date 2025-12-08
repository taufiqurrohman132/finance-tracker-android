package com.example.financetrackerapplication.domain.usecase

import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.domain.model.ItemAset2
import com.example.financetrackerapplication.domain.model.ItemTransaction

class GroupAsetUseCase {

    fun execute(listAset: List<AsetEntity>, order: List<String>) : List<ItemAset2>{

        val grouped = listAset.groupBy {
            // convert dati millis ke date
            it.groupAset
        }.toList().sortedBy { (key, _) -> order.indexOf(key)}.toMap()// balik urutan dari yang terbaru
        val result = mutableListOf<ItemAset2>() // memori sementara

        for ((group, items) in grouped){
            val itemsDesc = items.reversed()// urutkan dari yang terbaru
            val totalBalance = items.filter {
                it.groupAset == group
            }.sumOf { it.initialBalance }

            // header
            result.add(
                ItemAset2(
                    type = ItemTransaction.HEADER,
                    groupAset = group,
                    total = totalBalance
                )
            )

            // list item
            for (aset in itemsDesc){
                result.add(
                    ItemAset2(
                        id = aset.id,
                        type = ItemTransaction.ITEM,
                        groupAset = group,
                        initialBalance = aset.initialBalance,
                        name = aset.name
                    )
                )
            }
        }
        return result
    }
}