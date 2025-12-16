package com.example.financetrackerapplication.features.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.domain.model.ItemTransaction
import com.example.financetrackerapplication.domain.repository.TransactionRepository
import com.example.financetrackerapplication.domain.usecase.CalculateTotalBalanceUseCase
import com.example.financetrackerapplication.domain.usecase.GroupTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: TransactionRepository,
    private val groupTransactionsUseCase: GroupTransactionsUseCase,
    private val calculateTotalBalanceUseCase: CalculateTotalBalanceUseCase
) : ViewModel() {

    //    val listTransaction: LiveData<List<ItemTransaction>> = liveData {
//        repository.getAllTransaction().collect{ transaction ->
//            emit(groupTransactionsUseCase.execute(transaction))
//        }
//
    //    }
    private var transaction: List<ItemTransaction> = emptyList()

    private val _listTransaction = MutableLiveData<List<ItemTransaction>>()
    val listTransaction: LiveData<List<ItemTransaction>> = _listTransaction

    val totalBalance: LiveData<Long> =
        calculateTotalBalanceUseCase().asLiveData()

    init {
        viewModelScope.launch {
            repository.getAllTransaction().collect { list ->
                transaction = groupTransactionsUseCase.execute(list)
                _listTransaction.value = transaction
            }
        }
    }

    fun toggleSelect(itemTransaction: ItemTransaction) {
        transaction = transaction.map { trans ->
            if (trans.id == itemTransaction.id)
                trans.copy(isSelected = !trans.isSelected)
            else trans
        }
        _listTransaction.value = transaction
    }

    fun hasSelection(): Boolean {
        return transaction.any { trans ->
            trans.isSelected
        }
    }

    fun clearSelection() {
        transaction = transaction.map {
            it.copy(isSelected = false)
        }
        _listTransaction.value = transaction
    }

    fun selectAll() {
        transaction = transaction.map {
            it.copy(isSelected = true)
        }
        _listTransaction.value = transaction
    }

    fun deleteList(listAset: List<TransactionEntity>){
        viewModelScope.launch {
            repository.deleteTransaction(*listAset.toTypedArray())
        }
    }
}