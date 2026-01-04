package com.example.financetrackerapplication.domain.repository

import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionWithCategoryAndAccount
import com.example.financetrackerapplication.domain.model.DailyIncome
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransaction(): Flow<List<TransactionWithCategoryAndAccount>>
    fun getTransaction(id: Long): Flow<TransactionWithCategoryAndAccount>

    fun getDailyIncome(year: String, month: String, typeIncome: String): Flow<List<DailyIncome>>

    fun getTotalByDay(
        startDayMillis: Long,
        endDayMillis: Long
    ): Flow<Long>

    suspend fun insertTransaction(transaction: TransactionEntity)

    suspend fun updateTransaction(transaction: TransactionEntity)

    suspend fun deleteTransaction(vararg transaction: TransactionEntity)


}