package com.example.financetrackerapplication.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionWithCategoryAndAccount
import com.example.financetrackerapplication.domain.model.DailyIncome
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `transaction`")
    fun getAllTransaction(): Flow<List<TransactionWithCategoryAndAccount>>

    @Query("SELECT * FROM `transaction` WHERE id = :id ")
    fun getTransaction(id: Long): Flow<TransactionWithCategoryAndAccount>

    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(vararg transaction: TransactionEntity)

    @Query("""
        SELECT 
            CAST(strftime('%d', datetime(dateTimeMillis / 1000, 'unixepoch')) AS INTEGER) AS day,
            SUM(amount) AS total
        FROM `transaction`
        WHERE type = :incomeType
          AND strftime('%Y', datetime(dateTimeMillis / 1000, 'unixepoch')) = :year
          AND strftime('%m', datetime(dateTimeMillis / 1000, 'unixepoch')) = :month
        GROUP BY day
        ORDER BY day
    """)
    fun getDailyIncome(
        year: String,      // contoh: "2025"
        month: String,     // contoh: "01" s/d "12"
        incomeType: Int
    ): List<DailyIncome>

}