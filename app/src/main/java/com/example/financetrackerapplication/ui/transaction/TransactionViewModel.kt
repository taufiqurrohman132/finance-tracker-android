package com.example.financetrackerapplication.features.transaction

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.data.datasource.local.entity.CategoryEntity
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionWithCategoryAndAccount
import com.example.financetrackerapplication.domain.repository.AsetRapository
import com.example.financetrackerapplication.domain.repository.CategoryRapository
import com.example.financetrackerapplication.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repoTransaction: TransactionRepository,
    private val repoAset: AsetRapository,
    private val repoCategory: CategoryRapository,
) : ViewModel() {

    fun insertTransaction(
        amount: Long,
        type: String,
        dateTimeMillis: Long,
        description: String,
        photoDescription: List<Uri>,
        catatan: String,
        accountId: Long,
        categoryId: Long,
    ) {
        val transaction = TransactionEntity(
            amount = amount,
            type = type,
            dateTimeMillis = dateTimeMillis,
            description = description,
            photoDescription = photoDescription,
            catatan = catatan,
            accountId = accountId,
            categoryId = categoryId
        )
        viewModelScope.launch {
            repoTransaction.insertTransaction(transaction)
        }
    }

    fun updateTransaction(
        id: Long,
        amount: Long,
        type: String,
        dateTimeMillis: Long,
        catatan: String,
        photoDescription: List<Uri>,
        description: String,
        accountId: Long,
        categoryId: Long,
    ) {
        val transaction = TransactionEntity(
            id = id,
            amount = amount,
            type = type,
            dateTimeMillis = dateTimeMillis,
            description = description,
            photoDescription = photoDescription,
            catatan = catatan,
            accountId = accountId,
            categoryId = categoryId
        )
        viewModelScope.launch {
            repoTransaction.updateTransaction(transaction)
        }
    }

    fun getTransaction(id: Long): StateFlow<TransactionWithCategoryAndAccount?> {
        return repoTransaction.getTransaction(id)
            .stateIn(viewModelScope, SharingStarted.Lazily, null)
    }

    fun deleteTransaction(vararg transactionEntity: TransactionEntity) {
        viewModelScope.launch {
            repoTransaction.deleteTransaction(*transactionEntity)
        }
    }

    // aset and category
    suspend fun getAllAset(): List<AsetEntity> =
        repoAset.getAset().first()

    suspend fun getAllCategory(): List<CategoryEntity> =
        repoCategory.getCategory().first()

}