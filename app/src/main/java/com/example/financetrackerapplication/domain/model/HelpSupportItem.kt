package com.example.financetrackerapplication.domain.model

sealed class HelpSupportItem {
    data class Text(val text: String) : HelpSupportItem()
    data class Images(val resId: Int) : HelpSupportItem()
}