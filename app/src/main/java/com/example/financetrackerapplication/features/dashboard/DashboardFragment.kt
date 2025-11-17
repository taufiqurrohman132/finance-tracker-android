package com.example.financetrackerapplication.features.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financetrackerapplication.databinding.FragmentDashboardBinding
import com.example.financetrackerapplication.features.transaction.TransactionActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var dashoardAdapter: DashboardAdapter
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListener()
        observer()
    }

    private fun setupRecyclerView(){
        dashoardAdapter = DashboardAdapter(
            onClickItemHeader = { transaction ->

            },
            onClickItemBody = { transaction ->

            }
        )

        binding.dashRvTransaksi.apply {
            adapter = dashoardAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    // semua aksi listener di semua komponen
    private fun setupListener(){
        binding.tesss.setOnClickListener {
            val intent = Intent(requireActivity(), TransactionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observer(){
        viewModel.listTransaction.observe(viewLifecycleOwner){ listItem ->
            dashoardAdapter.submitList(listItem)
        }
    }

    private fun setupChart(){

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = DashboardFragment::class.java.simpleName
    }
}