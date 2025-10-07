package com.example.financetrackerapplication.data.repository

import com.example.financetrackerapplication.data.datasource.local.dao.TransactionDao
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
): TransactionRepository {
    override fun getAllTransaction(): Flow<List<TransactionEntity>> =
        transactionDao.getAllTransaction()

    override suspend fun insertTransaction(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
    }

    override suspend fun updateTransaction(transaction: TransactionEntity) {
        transactionDao.updateTransaction(transaction)
    }

    override suspend fun deleteTransaction(vararg transaction: TransactionEntity) {
        transactionDao.deleteTransaction(*transaction)
    }

}