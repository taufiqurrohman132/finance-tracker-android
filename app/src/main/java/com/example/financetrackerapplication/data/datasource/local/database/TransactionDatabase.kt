package com.example.financetrackerapplication.data.datasource.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.financetrackerapplication.data.datasource.local.entity.AccountEntity

@Database(
    entities = [AccountEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TransactionDatabase : RoomDatabase() {

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