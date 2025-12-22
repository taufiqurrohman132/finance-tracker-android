package com.example.financetrackerapplication.domain.usecase

import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionWithCategoryAndAccount
import com.example.financetrackerapplication.domain.model.ItemTransaction
import com.example.financetrackerapplication.utils.TimeUtils
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId

class GroupTransactionsUseCase {

    fun execute(listTransaction: List<TransactionWithCategoryAndAccount>, month: Month, year: Int) : List<ItemTransaction>{
        val grouped = setMonthYear(listTransaction, month, year).groupBy {
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
                        dataItem = tx
                    )
                )
            }
        }
        return result
    }

    private fun setMonthYear(
        list: List<TransactionWithCategoryAndAccount>,
        month: Month ,
        year: Int
    ) : List<TransactionWithCategoryAndAccount>{
        val zone = ZoneId.systemDefault()
        val start = LocalDate.of(year, month, 1)
            .atStartOfDay(zone)
            .toInstant()
            .toEpochMilli()

        val end = LocalDate.of(year, month, 1)
            .plusMonths(1)
            .atStartOfDay(zone)
            .toInstant()
            .toEpochMilli() - 1

        return list.filter { it.transaction.dateTimeMillis in start..end }
    }
}