package com.example.financetrackerapplication.domain.usecase

import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalculateTotalBalanceUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<Long> {
        return repository.getAllTransaction().map { list ->
            val totalIncome = list.filter { it.transaction.type == TransactionEntity.TYPE_INCOME }
                .sumOf { it.transaction.amount }
            val totalExpense = list.filter { it.transaction.type == TransactionEntity.TYPE_EXPANSE }
                .sumOf { it.transaction.amount }

            totalIncome - totalExpense
        }
    }
}