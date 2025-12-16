package com.example.financetrackerapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemTransaction(
    val id: Long? = null,
    val type: Int, // identitas HEADER / ITEM

    // type = HEADER
    val dateTimeMillis: Long? = null,
    val day: String? = null,
    val income: Long? = null,
    val expense: Long? = null,

    // type = ITEM
    val category: String? = null,
    val catatan: String? = null,
    val aset: String? = null,
    val isSelected: Boolean = false,
    val typeBalance: String? = null,
    val amount: Long? = null,
    val percentage: Double? = null
): Parcelable {
    companion object {
        const val HEADER = 0
        const val ITEM = 1
    }
}
