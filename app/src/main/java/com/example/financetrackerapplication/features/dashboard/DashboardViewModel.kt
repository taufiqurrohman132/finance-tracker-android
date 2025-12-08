package com.example.financetrackerapplication.features.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.room.PrimaryKey
import com.example.financetrackerapplication.domain.model.ItemTransaction
import com.example.financetrackerapplication.domain.repository.TransactionRepository
import com.example.financetrackerapplication.domain.usecase.CalculateTotalBalanceUseCase
import com.example.financetrackerapplication.domain.usecase.GroupTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.security.PrivateKey
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: TransactionRepository,
    private val groupTransactionsUseCase: GroupTransactionsUseCase,
    private val calculateTotalBalanceUseCase: CalculateTotalBalanceUseCase
) : ViewModel(){

    val listTransaction: LiveData<List<ItemTransaction>> = liveData {
        repository.getAllTransaction().collect{ transaction ->
            emit(groupTransactionsUseCase.execute(transaction))
        }
    }

    val totalBalance: LiveData<Long> =
        calculateTotalBalanceUseCase().asLiveData()



}