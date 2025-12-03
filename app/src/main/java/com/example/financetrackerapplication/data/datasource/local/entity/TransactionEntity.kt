package com.example.financetrackerapplication.data.datasource.local.entity

import android.net.Uri
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "transaction",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = [Table.CATEGORY_ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AsetEntity::class,
            parentColumns = ["id"],
            childColumns = [Table.ACCOUNT_ID],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(Table.CATEGORY_ID), Index(Table.ACCOUNT_ID)] // biar query lebih cepat
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val amount: Long = 0, // Jumlah uang
    val type: String, // "INCOME" atau "EXPENSE"
    val dateTimeMillis: Long, // Simpan sebagai timestamp (Long) agar mudah di-query
    val catatan: String?,
    val description: String?, //, bisa null
    val photoDescription: List<Uri> = emptyList(), //, bisa null
    val percentage: Double = 0.0, // 1.89
    val synced: Boolean = false, // tanda sinkronisasi

    val categoryId: Long,
    val accountId: Long
){
    companion object{
        const val TYPE_INCOME = "type_income"
        const val TYPE_EXPANSE = "type_expanse"
    }
}

data class TransactionWithCategoryAndAccount(
    @Embedded
    val transaction: TransactionEntity,

    @Relation(
        parentColumn = Table.CATEGORY_ID,
        entityColumn = "id"
    )
    val category: CategoryEntity,

    @Relation(
        parentColumn = Table.ACCOUNT_ID,
        entityColumn = "id"
    )
    val account: AsetEntity
)

//id table
object Table {
    const val CATEGORY_ID = "categoryId"
    const val ACCOUNT_ID = "accountId"
}
