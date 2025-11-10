package com.example.financetrackerapplication.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.financetrackerapplication.domain.model.TransOptions

@Entity(tableName = "categories")
data class CategoryEntity (
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,

    val categoryType: String, // "INCOME" atau "EXPENSE"
    override val name: String, // "Gaji", "Makanan", "Transportasi"
    override val iconName: String?, // Opsional: nama ikon untuk ditampilkan di UI
): TransOptions{
}