package com.example.financetrackerapplication.features.transaction.instalment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.financetrackerapplication.databinding.ActivityAddInstalmentBinding

class AddInstalmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddInstalmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddInstalmentBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}