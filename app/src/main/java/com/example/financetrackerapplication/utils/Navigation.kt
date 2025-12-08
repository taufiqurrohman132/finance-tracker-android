package com.example.financetrackerapplication.utils

import android.content.Context
import android.content.Intent
import com.example.financetrackerapplication.features.transaction.TransactionActivity

object Navigation {
    fun navigateToTransaction(context: Context, id: Long?){
        val intent = Intent(context, TransactionActivity::class.java)
        intent.putExtra(TransactionActivity.TRANS_ID, id)
        context.startActivity(intent)
    }
}