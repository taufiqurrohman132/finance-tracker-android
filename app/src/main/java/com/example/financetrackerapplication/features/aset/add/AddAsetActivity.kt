package com.example.financetrackerapplication.features.aset.add

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.databinding.ActivityAddAsetBinding
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
    }

    private fun setupListener() {
        binding.apply {
            addAssetBtnSave.setOnClickListener { saveAset() }
            addAssetEtGrup.setOnClickListener { showGroupOptionDialog() }


        }
    }

    private fun saveAset() {
        binding.apply {
            val total = addAssetEtTotal.text.toString()
                .toDoubleOrNull() // null ketika tidak bisa di convert
            if (total == null) {
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
                    addAssetEtName.postDelayed({
                        addAssetEtName.isFocusableInTouchMode = true
                        addAssetEtName.requestFocus()
                        val imm =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(addAssetEtName.windowToken, 0)
                        imm.showSoftInput(addAssetEtName, InputMethodManager.SHOW_FORCED)
                    }, 300)
                }
            }
            .show()
    }

    companion object {
        val TAG_ASET_ADD: String = AddAsetActivity::class.java.simpleName
    }

}