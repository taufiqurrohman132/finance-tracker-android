package com.example.financetrackerapplication.ui.settings.category.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.databinding.ActivityCategoryBinding
import com.example.financetrackerapplication.ui.settings.category.add.AddCategoryActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private lateinit var categoryAdapter: CategoryAdapter

    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.fabAddAset.setOnClickListener {
            val intent = Intent(this, AddCategoryActivity::class.java)
            startActivity(intent)
        }

        setupRecyclerView()
        setupListener()
        observer()

    }

    private fun setupListener() {

        binding.apply {
            btnBatalkanSelection.setOnClickListener {
                viewModel.clearSelection()
            }
            btnPilihSemuaSelection.setOnClickListener {
                viewModel.selectAll()
            }


            catBottomNavAction.setOnItemSelectedListener { menu ->
                when(menu.itemId){
                    R.id.action_delete_list -> {
                        val categoriesEntitiesToDelete = viewModel.listCategory.value
                            ?.filter { it.isSelected }
                            ?.map { it.data }
                            ?: emptyList()
                        Log.d(TAG_CATEGORY_ADD, "setupListener: delet list = $categoriesEntitiesToDelete")
                        viewModel.deleteList(categoriesEntitiesToDelete)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun observer() {
        viewModel.apply {
            listCategory.observe(this@CategoryActivity){ listCategory ->
                categoryAdapter.submitList(listCategory)

                binding.selectionLayout.isVisible = viewModel.hasSelection()
                binding.collapsingLayout.collapsedTitleGravity = if (viewModel.selectedCount() == 0)
                    Gravity.START else Gravity.CENTER_HORIZONTAL
                binding.collapsingLayout.title = if (viewModel.selectedCount() == 0)
                    "Aset" else "${viewModel.selectedCount()} dipilih"

                binding.catBottomNavAction.apply {
                    isVisible = viewModel.hasSelection()
                    animate()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter(
            onSelect = { data ->
                viewModel.toggleSelect(data.data.id)
            },
            onClicked = { data ->
                Intent(this, AddCategoryActivity::class.java).apply {
                    Log.d(TAG_CATEGORY_ADD, "setupRecyclerView: id = ${data.data.id}")
                    this.putExtra(AddCategoryActivity.EXTRA_CATEGORY, data.data.id)
                    startActivity(this)
                }
            }
        )

        binding.asetRvAset.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(this@CategoryActivity)
            setHasFixedSize(true)
        }


    }


    companion object {
        val TAG_CATEGORY_ADD: String = CategoryActivity::class.java.simpleName
    }
}