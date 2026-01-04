package com.example.financetrackerapplication.features.settings.categorymanage.add

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.data.datasource.local.entity.CategoryEntity
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.databinding.ActivityAddCategoryBinding
import com.example.financetrackerapplication.features.settings.categorymanage.list.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCategoryBinding
    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListener()
    }

    private fun setupListener() {
        binding.apply {
            btnSaveCategory.setOnClickListener{ saveCategory() }
        }
    }

    private fun saveCategory(){
        binding.apply {
            viewModel.saveCategory(
                name = inputNameCategory.text.toString(),
                iconName = null,
                categoryType = TransactionEntity.TYPE_INCOME
            )
            finish()
        }
    }
}