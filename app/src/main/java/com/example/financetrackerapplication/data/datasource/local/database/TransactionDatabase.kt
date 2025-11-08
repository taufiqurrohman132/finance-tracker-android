package com.example.financetrackerapplication.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.financetrackerapplication.data.datasource.local.dao.AsetDao
import com.example.financetrackerapplication.data.datasource.local.dao.CategoryDao
import com.example.financetrackerapplication.data.datasource.local.dao.TransactionDao
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.data.datasource.local.entity.CategoryEntity
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity

@Database(
    entities = [AsetEntity::class, CategoryEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun asetDao(): AsetDao
    abstract fun categoryDao(): CategoryDao

}