package com.example.financetrackerapplication.domain.usecase

import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionWithCategoryAndAccount
import com.example.financetrackerapplication.domain.model.ItemTransaction
import com.example.financetrackerapplication.utils.TimeUtils

class GroupTransactionsUseCase {

    fun execute(listTransaction: List<TransactionWithCategoryAndAccount>) : List<ItemTransaction>{
        val grouped = listTransaction.groupBy {
            // convert dati millis ke date
            TimeUtils.getDate(it.transaction.dateTimeMillis)
        }.toSortedMap(reverseOrder())// balik urutan dari yang terbaru
        val result = mutableListOf<ItemTransaction>() // memori sementara

        for ((date, items) in grouped){
            val itemsDesc = items.reversed()// urutkan dari yang terbaru
            val totalIncome = items.filter {
                it.transaction.type == TransactionEntity.TYPE_INCOME
            }.sumOf { it.transaction.amount }

            val totalExpanse = items.filter {
                it.transaction.type == TransactionEntity.TYPE_EXPANSE
            }.sumOf { it.transaction.amount }

            // header
            result.add(
                ItemTransaction(
                    type = ItemTransaction.HEADER,
                    dateTimeMillis = itemsDesc.first().transaction.dateTimeMillis,
                    day = TimeUtils.getDayName(date),
                    income = totalIncome,
                    expense = totalExpanse
                )
            )

            // list item
            for (tx in itemsDesc){
                result.add(
                    ItemTransaction(
                        id = tx.transaction.id,
                        type = ItemTransaction.ITEM,
                        category = tx.category.name,
                        amount = tx.transaction.amount,
                        catatan = tx.transaction.catatan,
                        typeBalance = tx.transaction.type,
                        aset = tx.account.name,
                        percentage = tx.transaction.percentage
                    )
                )
            }
        }
        return result
    }
}