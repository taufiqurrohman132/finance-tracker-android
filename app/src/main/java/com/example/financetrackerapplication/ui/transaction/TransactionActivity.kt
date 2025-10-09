package com.example.financetrackerapplication.ui.transaction

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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


    }

    private fun setupListener() {

    }

    private fun saveTransaction(){

    }

    private fun observer(){

    }

}