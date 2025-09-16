package com.example.financetrackerapplication.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String, // "Bank BCA", "GoPay", "Tunai"
    val initialBalance: Double, // Saldo awal saat membuat akun
    val iconName: String? // Opsional: nama ikon (misal: logo GoPay)
)