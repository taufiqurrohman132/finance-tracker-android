package com.example.financetrackerapplication.domain.model

import android.os.Parcelable
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionWithCategoryAndAccount
import kotlinx.parcelize.Parcelize

data class ItemTransaction(
    val id: Long? = null,
    val type: Int, // identitas HEADER / ITEM

    // type = HEADER
    val dateTimeMillis: Long? = null,
    val day: String? = null,
    val income: Long? = null,
    val expense: Long? = null,

    // type = ITEM
    val dataItem: TransactionWithCategoryAndAccount? = null,
    val isSelected: Boolean = false
) {
    companion object {
        const val HEADER = 0
        const val ITEM = 1
    }
}
