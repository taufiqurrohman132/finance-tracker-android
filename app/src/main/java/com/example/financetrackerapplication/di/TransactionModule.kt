package com.example.financetrackerapplication.di

import com.example.financetrackerapplication.data.datasource.local.dao.TransactionDao
import com.example.financetrackerapplication.data.datasource.local.database.TransactionDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TransactionModule {
//    @Provides
//    fun provideTransactionDao(): TransactionDao =

}