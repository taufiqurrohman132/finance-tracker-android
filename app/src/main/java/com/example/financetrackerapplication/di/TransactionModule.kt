package com.example.financetrackerapplication.di

import com.example.financetrackerapplication.data.datasource.local.dao.TransactionDao
import com.example.financetrackerapplication.data.datasource.local.database.TransactionDatabase
import com.example.financetrackerapplication.domain.repository.TransactionRepository
import com.example.financetrackerapplication.domain.usecase.CalculateTotalBalanceUseCase
import com.example.financetrackerapplication.domain.usecase.GroupTransactionsUseCase
import com.example.financetrackerapplication.features.transaction.TransactionViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TransactionModule {
    @Provides
    fun provideGroupTransactionsUseCase(): GroupTransactionsUseCase =
        GroupTransactionsUseCase()

    @Provides
    fun provideCalculateTotalBalanceUseCase(repository: TransactionRepository) =
        CalculateTotalBalanceUseCase(repository)
}