package com.example.financetrackerapplication.domain.model

data class ItemTransaction(
    val type: Int, // identitas HEADER / ITEM

    // type = HEADER
    val date: String? = null,
    val day: String? = null,
    val income: Long? = null,
    val expense: Long? = null,

    // type = ITEM
    val category: String? = null,
    val catatan: String? = null,
    val aset: String? = null,
    val amount: Long? = null,
    val percentage: Double? = null
) {
    companion object {
        const val HEADER = 0
        const val ITEM = 1
    }
}
