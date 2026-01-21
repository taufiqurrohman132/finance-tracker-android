package com.example.financetrackerapplication.ui.settings.category.add

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.databinding.ActivityAddCategoryBinding
import com.example.financetrackerapplication.ui.settings.category.list.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCategoryBinding
    private val viewModel: CategoryViewModel by viewModels()

    private var categoryId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setupListener()
        observer()
    }

    private fun observer() {
        Log.d(TAG, "observer: category id = $categoryId")
        categoryId?.let { id ->
            lifecycleScope.launch {
                viewModel.getCategory(id).collect { category ->
                    binding.apply {
                        addEtCategoryName.setText(category?.name)
                    }
                }
            }
        }
    }

    private fun setupListener() {
        binding.apply {
            addBtnSave.setOnClickListener {
                saveCategory()
                finish()
            }
            addBtnNextLanjut.setOnClickListener { saveCategory() }
        }
    }

    private fun init() {
        categoryId = intent.getSerializableExtra(EXTRA_CATEGORY) as? Long
        Log.d(TAG, "init: inisialize categori id = $categoryId")
        binding.apply {
            // setup name text button
            addBtnSave.text = resources.getString(
                categoryId?.let { R.string.update } ?: R.string.simpan
            )
        }
    }

    private fun saveCategory() {
        binding.apply {
            viewModel.saveCategory(
                name = addEtCategoryName.text.toString(),
                iconName = null,
                categoryType = TransactionEntity.TYPE_INCOME
            )
            addEtCategoryName.apply {
                setText("")
                requestFocus()
            }
        }
    }

    companion object {
        val EXTRA_CATEGORY = "extra_category"
        private val TAG = AddCategoryActivity::class.java.simpleName
    }
}