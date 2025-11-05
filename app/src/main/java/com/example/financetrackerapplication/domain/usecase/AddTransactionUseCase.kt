package com.example.financetrackerapplication.domain.usecase

import androidx.room.Transaction
import com.example.financetrackerapplication.domain.repository.AsetRapository
import com.example.financetrackerapplication.domain.repository.CategoryRapository
import com.example.financetrackerapplication.domain.repository.TransactionRepository

class AddTransactionUseCase(
    private val repoAset: AsetRapository,
    private val repoCategory: CategoryRapository,
    private val repoTransaction: TransactionRepository,
) {
    fun insertTransaction(){

    }
}