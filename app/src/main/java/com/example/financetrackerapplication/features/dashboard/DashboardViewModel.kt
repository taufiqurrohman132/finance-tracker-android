package com.example.financetrackerapplication.features.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.room.PrimaryKey
import com.example.financetrackerapplication.domain.model.ItemTransaction
import com.example.financetrackerapplication.domain.repository.TransactionRepository
import com.example.financetrackerapplication.domain.usecase.GroupTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.security.PrivateKey
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: TransactionRepository,
    private val groupTransactionsUseCase: GroupTransactionsUseCase
) : ViewModel(){

//    val listTransaction: LiveData<List<ItemTransaction>> =
//        groupTransactionsUseCase.execute(repository.getAllTransaction())
}