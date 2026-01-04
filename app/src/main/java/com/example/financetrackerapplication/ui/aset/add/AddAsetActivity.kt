package com.example.financetrackerapplication.ui.aset.add

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.databinding.ActivityAddAsetBinding
import com.example.financetrackerapplication.utils.DialogUtils
import com.example.financetrackerapplication.utils.Extention.hideKeyboard
import com.example.financetrackerapplication.utils.Extention.parseMoneyToLong
import com.example.financetrackerapplication.utils.Extention.showKeyboard
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAsetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAsetBinding
    private val viewModel: AddAsetViewModel by viewModels()

    // value default awal
    private var selectedGroupIndex = 0
    private lateinit var items: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAsetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setupListener()

    }

    private fun init() {
        items = resources.getStringArray(R.array.group_option_aset)
        binding.addAssetEtGrup.setText(items[selectedGroupIndex])
        showGroupOptionDialog()

        /* pastikan keyboard tidak muncul */
        binding.addAssetEtTotal.apply {
            showSoftInputOnFocus = false
            inputType = InputType.TYPE_NULL // pastikan keyboard tidak muncul
        }


    }

    private fun setupListener() {
        binding.apply {
            addAssetBtnSave.setOnClickListener { saveAset() }
            addAssetEtGrup.setOnClickListener { showGroupOptionDialog() }

            addAssetEtTotal.setOnFocusChangeListener { _, hasFocus ->
                keyboardNum.root.apply {
                    isVisible = hasFocus
                }
            }

            // hide keyboard on non focus
            addAssetEtName.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) addAssetEtName.hideKeyboard(this@AddAsetActivity)
                else addAssetEtName.showKeyboard(this@AddAsetActivity)
            }
            addAssetEtDeskripsi.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) addAssetEtDeskripsi.hideKeyboard(this@AddAsetActivity)
                else addAssetEtDeskripsi.showKeyboard(this@AddAsetActivity)
            }

            // setup keyboard
            DialogUtils.keyboardClickListener(
                this@AddAsetActivity,
                {
                    // ketiak btn selesai di triger
                    addAssetEtDeskripsi.requestFocus()
                },
                addAssetEtTotal,
                keyboardNum.keyNum1,
                keyboardNum.keyNum2,
                keyboardNum.keyNum3,
                keyboardNum.keyNum4,
                keyboardNum.keyNum5,
                keyboardNum.keyNum6,
                keyboardNum.keyNum7,
                keyboardNum.keyNum8,
                keyboardNum.keyNum9,
                keyboardNum.keyNumNol,
                keyboardNum.keyNumKoma,
                keyboardNum.keyNumMinus,
                keyboardNum.keyNumDelete,
                keyboardNum.keyNumCalculator,
                keyboardNum.keyNumSelesai
            )

        }
    }

    private fun saveAset() {
        binding.apply {
            val total = addAssetEtTotal.text.toString()
                .parseMoneyToLong() // null ketika tidak bisa di convert
            if (total == 0L) {
                // toast
                Toast.makeText(
                    this@AddAsetActivity,
                    "mohon isi dengan nilai yang benar",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            viewModel.saveAset(
                addAssetEtName.text.toString(),
                total,
                addAssetEtGrup.text.toString(),
                null
            )

            Log.d(TAG_ASET_ADD, "saveAset: total edit teks = $total")
            finish()
        }
    }

    private fun showGroupOptionDialog() {
        MaterialAlertDialogBuilder(this, R.style.CustomAlertDialog)
            .setTitle(R.string.grup_aset)
            .setSingleChoiceItems(items, selectedGroupIndex) { dialog, which ->
                selectedGroupIndex = which
                binding.addAssetEtGrup.setText(items[selectedGroupIndex])
                dialog.dismiss()
            }
            .setOnDismissListener {
                binding.apply {
                    // langsung fokus edit name
                    Log.d(TAG_ASET_ADD, "showGroupOptionDialog: aset et name request focus")
                    binding.addAssetEtName.post {
                        binding.addAssetEtName.apply {
                            requestFocus()
                            showKeyboard(this@AddAsetActivity)
                        }
                    }
                }
            }
            .show()
    }

    companion object {
        val TAG_ASET_ADD: String = AddAsetActivity::class.java.simpleName
    }

}