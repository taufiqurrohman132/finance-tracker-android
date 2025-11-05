package com.example.financetrackerapplication.features.transaction

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.databinding.ActivityTransactionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionBinding

    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListener()
        observer()
    }

    private fun setupListener() {

    }

    private fun saveTransaction(){
        binding.apply {
            viewModel.insertTransaction(
                amount = addEtTotal.text.toString().toDouble(),
                type = if (buttonGroupTypeTransaction.position == 0) TransactionEntity.TYPE_INCOME
                else TransactionEntity.TYPE_EXPANSE,
                dateTimeMillis = System.currentTimeMillis(),
                description = addEtDeskripsi.text.toString(),

            )
        }
    }

    private fun observer(){

    }

}