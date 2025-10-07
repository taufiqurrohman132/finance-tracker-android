package com.example.financetrackerapplication.domain.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransaction(): Flow<List<TransactionEntity>>

    suspend fun insertTransaction(transaction: TransactionEntity)

    suspend fun updateTransaction(transaction: TransactionEntity)

    suspend fun deleteTransaction(vararg transaction: TransactionEntity)


}