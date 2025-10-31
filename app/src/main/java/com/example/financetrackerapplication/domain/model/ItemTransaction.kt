package com.example.financetrackerapplication.domain.model

data class ItemTransaction(
    val type: Int, // identitas HEADER / ITEM

    // type = HEADER
    val date: String? = null,
    val day: String? = null,
    val income: Double? = null,
    val expense: Double? = null,

    // type = ITEM
    val category: String? = null,
    val catatan: String? = null,
    val aset: String? = null,
    val amount: Double? = null,
    val percentage: Double? = null
) {
    companion object {
        const val HEADER = 0
        const val ITEM = 1
    }
}
