package com.example.financetrackerapplication.features.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.data.datasource.local.entity.CategoryEntity
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.domain.repository.AsetRapository
import com.example.financetrackerapplication.domain.repository.CategoryRapository
import com.example.financetrackerapplication.domain.repository.TransactionRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class TransactionViewModel @Inject constructor(
    private val repoTransaction: TransactionRepository,
    private val repoAset: AsetRapository,
    private val repoCategory: CategoryRapository,
) : ViewModel() {

    // untuk beberap opsi aset
    val listAsetOptions: LiveData<List<AsetEntity>> =
        repoAset.getAset().asLiveData()

    val listCategoryOptions: LiveData<List<CategoryEntity>> =
        repoCategory.getCategory().asLiveData()

    fun insertTransaction(
        amount: Double,
        type: String,
        dateTimeMillis: Long,
        description: String,
        accountId: Long,
        categoryId: Long,
    ) {
        val transaction = TransactionEntity(
            amount = amount,
            type = type,
            dateTimeMillis = dateTimeMillis,
            description = description,
            accountId = accountId,
            categoryId = categoryId
        )
        viewModelScope.launch {
            repoTransaction.insertTransaction(transaction)
        }
    }

    fun updateTransaction(
        amount: Double,
        type: String,
        dateTimeMillis: Long,
        description: String,
        accountId: Long,
        categoryId: Long,
    ) {
        val transaction = TransactionEntity(
            amount = amount,
            type = type,
            dateTimeMillis = dateTimeMillis,
            description = description,
            accountId = accountId,
            categoryId = categoryId
        )
        viewModelScope.launch {
            repoTransaction.updateTransaction(transaction)
        }
    }

    fun deleteTransaction(vararg transactionEntity: TransactionEntity) {
        viewModelScope.launch {
            repoTransaction.deleteTransaction(*transactionEntity)
        }
    }


}