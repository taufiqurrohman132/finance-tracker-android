package com.example.financetrackerapplication.ui.settings.categorymanage.list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.financetrackerapplication.databinding.ActivityCategoryBinding
import com.example.financetrackerapplication.ui.settings.categorymanage.add.AddCategoryActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.fabAddCategory.setOnClickListener {
            val intent = Intent(this, AddCategoryActivity::class.java)
            startActivity(intent)
        }

    }


    companion object {
        val TAG_CATEGORY_ADD: String = CategoryActivity::class.java.simpleName
    }
}