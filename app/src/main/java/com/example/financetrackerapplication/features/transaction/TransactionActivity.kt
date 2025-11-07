package com.example.financetrackerapplication.features.transaction

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.data.datasource.local.entity.CategoryEntity
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.databinding.ActivityTransactionBinding
import com.example.financetrackerapplication.databinding.SheetAddTransAsetBinding
import com.example.financetrackerapplication.databinding.SheetAddTransCategoryBinding
import com.example.financetrackerapplication.utils.DialogUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionBinding

    private val viewModel: TransactionViewModel by viewModels()

    // adapter
    private lateinit var asetAdapter: OptionsAdapter<AsetEntity>
    private lateinit var categoryAdapter: OptionsAdapter<CategoryEntity>

    // input selected
    private var asetSelectedId: Long = -1
    private var categorySelectedId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListener()
        observer()
    }

    private fun setupListener() {
        binding.apply {
            addEtAset.setOnClickListener { showAsetBottomSheetDialog() }
            addEtKategori.setOnClickListener { showCategoryBottomSheetDialog() }

            addBtnSave.setOnClickListener { saveTransaction() }
        }
    }

    private fun setupRecyclerView() {
        // aset
        asetAdapter = OptionsAdapter {
            asetSelectedId = it.id
        }
        categoryAdapter = OptionsAdapter {
            categorySelectedId = it.id
        }
    }

    private fun saveTransaction(){
        binding.apply {
            viewModel.insertTransaction(
                amount = addEtTotal.text.toString().toDouble(),
                type = if (buttonGroupTypeTransaction.position == 0) TransactionEntity.TYPE_INCOME
                else TransactionEntity.TYPE_EXPANSE,
                dateTimeMillis = System.currentTimeMillis(),
                description = addEtDeskripsi.text.toString(),
                accountId = asetSelectedId,
                categoryId = categorySelectedId
            )
        }
    }

    private fun observer(){
        viewModel.apply {
            listCategoryOptions.observe(this@TransactionActivity) { category ->

            }
            listAsetOptions.observe(this@TransactionActivity) { aset ->

            }
        }
    }

    private fun showAsetBottomSheetDialog(){
        val bindingBottomSheet = SheetAddTransAsetBinding.inflate(layoutInflater)
        DialogUtils.showBottomSheet(this, bindingBottomSheet.root)

        // listener
        bindingBottomSheet.apply {
            transRvListAsetOptions.apply {
                adapter = asetAdapter
                layoutManager = GridLayoutManager(this@TransactionActivity, 3)
            }
        }
    }

    private fun showCategoryBottomSheetDialog(){
        val bindingBottomSheet = SheetAddTransCategoryBinding.inflate(layoutInflater)
        DialogUtils.showBottomSheet(this, bindingBottomSheet.root)

        // listener
        bindingBottomSheet.apply {
            transRvListCategoryOptions.apply {
                adapter = asetAdapter
                layoutManager = GridLayoutManager(this@TransactionActivity, 3)
            }
        }
    }

}