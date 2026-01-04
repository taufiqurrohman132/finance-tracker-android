package com.example.financetrackerapplication.features.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.financetrackerapplication.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}