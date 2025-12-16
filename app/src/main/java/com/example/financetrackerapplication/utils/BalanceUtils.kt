package com.example.financetrackerapplication.utils

import android.graphics.Color

object BalanceUtils {
    fun getBalanceColor(balance: Long): Int {
        return if (balance < 0) {
            Color.RED
        } else {
            Color.GREEN
        }
    }

}