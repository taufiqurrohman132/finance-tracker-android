package com.example.financetrackerapplication.di

import com.example.financetrackerapplication.domain.usecase.GroupTransactionsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TransactionModule {
    @Provides
    fun provideGroupTransactionsUseCase(): GroupTransactionsUseCase =
        GroupTransactionsUseCase()

}