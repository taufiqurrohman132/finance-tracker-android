package com.example.financetrackerapplication.features.transaction.repeat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.financetrackerapplication.databinding.ActivityAddRepeatBinding

class AddRepeatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRepeatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRepeatBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}