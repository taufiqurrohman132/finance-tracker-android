package com.example.financetrackerapplication.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val amount: Double, // Jumlah uang
    val type: String, // "INCOME" atau "EXPENSE"
    val date: Long, // Simpan sebagai timestamp (Long) agar mudah di-query
    val description: String?, // Catatan, bisa null

    // Foreign Key (Kunci Asing) yang menghubungkan ke tabel lain
    val categoryId: Long,
    val accountId: Long
)
