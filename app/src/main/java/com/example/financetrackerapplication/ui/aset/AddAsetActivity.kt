package com.example.financetrackerapplication.ui.aset

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.databinding.ActivityAddAsetBinding

class AddAsetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAsetBinding
    private val viewModel: AddAsetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAsetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListener()

    }

    private fun setupListener(){

    }

}