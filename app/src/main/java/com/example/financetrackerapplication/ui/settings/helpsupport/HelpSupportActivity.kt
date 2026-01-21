package com.example.financetrackerapplication.ui.settings.helpsupport

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.databinding.ActivityDetailHelpSupportBinding
import com.example.financetrackerapplication.databinding.ActivityHelpSupportBinding
import com.example.financetrackerapplication.domain.model.HelpSupportItem
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
        helpSupportAdapter= HelpSupportAdapter(
            onClickItem = {

            }
        )
        binding.asetRvHelpSupport.apply {
            adapter = helpSupportAdapter
            layoutManager =  LinearLayoutManager(this@HelpSupportActivity)
        }

        val listHelpSupport = listOf<HelpSupportItem>(
        )
    }


}