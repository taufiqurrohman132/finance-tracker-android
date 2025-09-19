package com.example.financetrackerapplication.ui.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.databinding.ActivitySignInLinkEmailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInLinkEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInLinkEmailBinding

    private val viewModel: SignInLinkEmailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInLinkEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListener()

    }

    private fun setupListener(){
        binding.linkEmailBtnLogin.setOnClickListener { viewModel.signInLinkEmail(email = binding.linkEmailEdtEmail.text.toString()) }
    }
}