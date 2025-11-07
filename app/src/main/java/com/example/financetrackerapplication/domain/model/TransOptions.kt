package com.example.financetrackerapplication.domain.model

// supaya bisa di pakai options adapter
interface TransOptions {
    val id: Long
    val name: String
    val iconName: String?
}