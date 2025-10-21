package com.example.financetrackerapplication.features.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.domain.repository.TransactionRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    fun insertTransaction(
        amount: Double,
        type: String,
        date: Long,
        description: String,
        accountId: Long,
        categoryId: Long,
    ) {
        val transaction = TransactionEntity(
            amount = amount,
            type = type,
            date = date,
            description = description,
            accountId = accountId,
            categoryId = categoryId
        )
        viewModelScope.launch {
            repository.insertTransaction(transaction)
        }
    }

    fun updateTransaction(
        amount: Double,
        type: String,
        date: Long,
        description: String,
        accountId: Long,
        categoryId: Long,
    ) {
        val transaction = TransactionEntity(
            amount = amount,
            type = type,
            date = date,
            description = description,
            accountId = accountId,
            categoryId = categoryId
        )
        viewModelScope.launch {
            repository.updateTransaction(transaction)
        }
    }

    fun deleteTransaction(vararg transactionEntity: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(*transactionEntity)
        }
    }


}