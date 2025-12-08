package com.example.financetrackerapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemAset2(
    val id: Long? = null,
    val type: Int, // identitas HEADER / ITEM
    val isVisible: Boolean = true,

    // type = HEADER
    val groupAset: String? = null, // HEADER and ITEM
    val total: Long? = null,

    // type = ITEM
    val name: String? = null,
    val initialBalance: Long? = null,
): Parcelable {
    companion object {
        const val HEADER = 0
        const val ITEM = 1
    }
}
