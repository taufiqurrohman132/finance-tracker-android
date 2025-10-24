package com.example.financetrackerapplication.di

import com.example.financetrackerapplication.data.repository.AsetRapositoryImpl
import com.example.financetrackerapplication.data.repository.AuthRepositoryImpl
import com.example.financetrackerapplication.data.repository.TransactionRepositoryImpl
import com.example.financetrackerapplication.domain.repository.AsetRapository
import com.example.financetrackerapplication.domain.repository.AuthRepository
import com.example.financetrackerapplication.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAsetRepository(
        asetRepositoryImpl: AsetRapositoryImpl
    ): AsetRapository

    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository

}