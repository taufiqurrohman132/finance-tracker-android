package com.example.financetrackerapplication.di

import android.content.Context
import androidx.room.Room
import com.example.financetrackerapplication.data.datasource.local.dao.AsetDao
import com.example.financetrackerapplication.data.datasource.local.dao.TransactionDao
import com.example.financetrackerapplication.data.datasource.local.database.TransactionDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTransactionDatabase(
        @ApplicationContext context: Context // Hilt sudah otomatis menyediakan ApplicationContext
    ) : TransactionDatabase = Room.databaseBuilder(
            context,
            TransactionDatabase::class.java,
            "transaction.database"
        ).build()

    @Provides
    fun provideAsetDao(database: TransactionDatabase): AsetDao =
        database.asetDao()

    @Provides
    fun provideTransactionDao(database: TransactionDatabase): TransactionDao =
        database.transactionDao()

}