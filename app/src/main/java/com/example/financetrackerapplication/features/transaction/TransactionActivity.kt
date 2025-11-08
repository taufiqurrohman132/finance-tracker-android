package com.example.financetrackerapplication.features.transaction

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.data.datasource.local.entity.CategoryEntity
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.databinding.ActivityTransactionBinding
import com.example.financetrackerapplication.databinding.SheetAddTransAsetBinding
import com.example.financetrackerapplication.databinding.SheetAddTransCategoryBinding
import com.example.financetrackerapplication.domain.model.TransOptions
import com.example.financetrackerapplication.utils.DialogUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
            addEtKategori.setOnClickListener {
                showBottomSheetDialog { dialog ->
                    val adapter = OptionsAdapter<CategoryEntity> {
                        categorySelectedId = it.id
                        dialog.dismiss()
                    }
                    lifecycleScope.launch {
                        val data = viewModel.getAllCategory()
                        adapter.submitList(data)
                    }
                    adapter
                }
            }
            addEtAset.setOnClickListener {
                showBottomSheetDialog { dialog ->
                    val adapter = OptionsAdapter<AsetEntity> {
                        asetSelectedId = it.id
                        dialog.dismiss()
                    }
                    lifecycleScope.launch {
                        val data = viewModel.getAllAset()
                        adapter.submitList(data)
                    }
                    adapter
                }
            }

            addBtnSave.setOnClickListener { saveTransaction() }
        }
    }

    private fun setupRecyclerView() {
        // aset


    }

    private fun saveTransaction() {
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

    private fun observer() {
        viewModel.apply {

        }
    }

    private fun <T : TransOptions> showBottomSheetDialog(optionsAdapter: (BottomSheetDialog) -> OptionsAdapter<T>) {
        val bindingBottomSheet = SheetAddTransCategoryBinding.inflate(layoutInflater)
        val dialog = DialogUtils.showBottomSheet(this, bindingBottomSheet.root)

        bindingBottomSheet.apply {
            transRvListCategoryOptions.apply {
                adapter = optionsAdapter.invoke(dialog)
                layoutManager = GridLayoutManager(this@TransactionActivity, 3)
            }
        }
    }

}