package com.example.financetrackerapplication.data.repository

import com.example.financetrackerapplication.data.datasource.local.dao.TransactionDao
import com.example.financetrackerapplication.domain.repository.TransactionRepository

class TransactionRepositoryImpl constructor(
    private val transactionDao: TransactionDao
): TransactionRepository {

}