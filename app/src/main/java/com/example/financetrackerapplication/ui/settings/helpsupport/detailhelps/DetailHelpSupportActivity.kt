package com.example.financetrackerapplication.ui.settings.helpsupport.detailhelps

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.databinding.ActivityCategoryBinding
import com.example.financetrackerapplication.databinding.ActivityDetailHelpSupportBinding
import com.example.financetrackerapplication.ui.settings.category.add.AddCategoryActivity
import com.example.financetrackerapplication.ui.settings.category.list.CategoryAdapter
import com.example.financetrackerapplication.ui.settings.category.list.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailHelpSupportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHelpSupportBinding
    private lateinit var detailHelpSupportAdapter: DetailHelpSupportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHelpSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}