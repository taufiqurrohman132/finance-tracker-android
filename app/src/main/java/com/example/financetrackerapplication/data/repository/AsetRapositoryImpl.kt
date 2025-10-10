package com.example.financetrackerapplication.data.repository

import com.example.financetrackerapplication.data.datasource.local.dao.TransactionDao
import com.example.financetrackerapplication.domain.repository.AsetRapository

class AsetRapositoryImpl(
    private val transactionDao: TransactionDao
): AsetRapository {
}