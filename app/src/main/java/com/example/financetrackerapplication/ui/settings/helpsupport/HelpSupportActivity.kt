package com.example.financetrackerapplication.ui.settings.helpsupport

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.databinding.ActivityHelpSupportBinding
import com.example.financetrackerapplication.domain.model.HelpSupportItem
import com.example.financetrackerapplication.ui.settings.category.list.CategoryActivity
import com.example.financetrackerapplication.ui.settings.helpsupport.detailhelps.DetailHelpSupportActivity
import com.example.financetrackerapplication.utils.ParseUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpSupportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpSupportBinding
    private lateinit var helpSupportAdapter: HelpSupportAdapter

    private val viewModel: HelpSupportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        helpSupportAdapter = HelpSupportAdapter(
            onClickItem = { titleResId ->
                Intent(this, DetailHelpSupportActivity::class.java).apply {
                    this.putExtra(DetailHelpSupportActivity.EXTRA_HELP_RES_ID, titleResId)
                    startActivity(this)
                }
            }
        )
        binding.asetRvHelpSupport.apply {
            adapter = helpSupportAdapter
            layoutManager = LinearLayoutManager(this@HelpSupportActivity)
        }

        val listHelp = listOf(
            R.string.help_backup,
            R.string.help_contact,
            R.string.help_income_1,
            R.string.help_income_2,
            R.string.help_expense_1,
            R.string.help_expense_2,
        )

//        val content= readRawFile(this, R.raw.help_expense)
//        val part = content.split("\n", limit = 2)
//        val title = part.first()
//        val body= part.getOrNull(1) ?: ""

        val imageMap = mapOf(
            "expense_1" to R.drawable.jenis,
            "expense_2" to R.drawable.jenis2
        )
//        val items = ParseUtils.parseHelpSupport(
//            content = content,
//            imageMap = imageMap
//        )

        helpSupportAdapter.submitList(listHelp)


    }

    private fun readRawFile(context: Context, @RawRes resId: Int): String{
        return context.resources.openRawResource(resId)
            .bufferedReader()
            .use { it.readText() }
    }




}