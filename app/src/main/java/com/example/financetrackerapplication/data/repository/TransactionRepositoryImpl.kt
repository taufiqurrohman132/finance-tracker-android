package com.example.financetrackerapplication.data.repository

import com.example.financetrackerapplication.data.datasource.local.dao.TransactionDao
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionWithCategoryAndAccount
import com.example.financetrackerapplication.domain.model.DailyIncome
import com.example.financetrackerapplication.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
): TransactionRepository {
    override fun getAllTransaction(): Flow<List<TransactionWithCategoryAndAccount>> =
        transactionDao.getAllTransaction()

    override fun getTransaction(id: Long): Flow<TransactionWithCategoryAndAccount> =
        transactionDao.getTransaction(id)

    override fun getDailyIncome(year: String, month: String, typeIncome: String): Flow<List<DailyIncome>> {
        return transactionDao.getDailyIncome(year, month, typeIncome)
    }

    override fun getTotalByDay(startDayMillis: Long, endDayMillis: Long): Flow<Long> {
        return transactionDao.getTotalByDay(startDayMillis, endDayMillis)
    }


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