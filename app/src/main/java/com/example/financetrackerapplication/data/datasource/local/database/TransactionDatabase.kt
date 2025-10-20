package com.example.financetrackerapplication.data.datasource.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.financetrackerapplication.data.datasource.local.dao.AsetDao
import com.example.financetrackerapplication.data.datasource.local.dao.TransactionDao
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity

@Database(
    entities = [AsetEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun asetDao(): AsetDao

    companion object{
        @Volatile
        private var instance: TransactionDatabase? = null

        fun getInstance(context: Context): TransactionDatabase =
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                TransactionDatabase::class.java,
                "transaction.database"
            ).build()
    }
}