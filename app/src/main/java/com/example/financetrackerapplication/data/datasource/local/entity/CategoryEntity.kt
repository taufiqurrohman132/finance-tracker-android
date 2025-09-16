package com.example.financetrackerapplication.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String, // "Gaji", "Makanan", "Transportasi"
    val type: String, // "INCOME" atau "EXPENSE" agar mudah difilter
    val iconName: String?, // Opsional: nama ikon untuk ditampilkan di UI
    val colorHex: String? // Opsional: kode warna hex untuk UI
)