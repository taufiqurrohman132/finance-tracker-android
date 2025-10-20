package com.example.financetrackerapplication.ui.aset

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.financetrackerapplication.databinding.ActivityAddAsetBinding

class AddAsetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAsetBinding
    private val viewModel: AddAsetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAsetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListener()

    }

    private fun observer(){

    }

    private fun setupListener() {
        binding.apply {
            addAssetBtnSave.setOnClickListener {
                val total = addAssetEtTotal.text.toString().toDoubleOrNull() // null ketika tidak bisa di convert
                if (total == null) {
                    // toast
                    Toast.makeText(
                        this@AddAsetActivity,
                        "mohon isi dengan nilai yang benar",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                viewModel.saveAset(
                    addAssetEtName.text.toString(),
                    total,
                    addAssetEtGrup.text.toString(),
                    null
                )
            }
        }
    }

    companion object {
        val TAG_ASET_ADD: String = AddAsetActivity::class.java.simpleName
    }

}