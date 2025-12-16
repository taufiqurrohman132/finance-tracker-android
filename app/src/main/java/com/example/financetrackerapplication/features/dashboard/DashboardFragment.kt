package com.example.financetrackerapplication.features.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financetrackerapplication.MainActivity
import com.example.financetrackerapplication.MainSharedViewModel
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.databinding.FragmentDashboardBinding
import com.example.financetrackerapplication.features.transaction.TransactionActivity
import com.example.financetrackerapplication.utils.Extention.parseLongToMoney
import com.example.financetrackerapplication.utils.Navigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var dashoardAdapter: DashboardAdapter
    private val viewModel: DashboardViewModel by viewModels()
    private val sharedViewModel: MainSharedViewModel by activityViewModels()

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

    private fun setupRecyclerView() {
        dashoardAdapter = DashboardAdapter(
            onClickItemHeader = { transaction ->
                val intent = Intent(requireContext(), TransactionActivity::class.java)
                intent.putExtra(TransactionActivity.TRANS_TIME_MILLIS, transaction.dateTimeMillis)
                startActivity(intent)
            },
            onClickItemBody = { transaction ->
                Navigation.navigateToTransaction(requireContext(), transaction.id)
            },
            onItemSelected = { item ->
                viewModel.toggleSelect(item)

            }
        )

        binding.dashRvTransaksi.apply {
            adapter = dashoardAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    // semua aksi listener di semua komponen
    private fun setupListener() {
        binding.dashFabAddTrans.setOnClickListener {
            val intent = Intent(requireActivity(), TransactionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observer() {
        viewModel.apply {
            // header total
            totalBalance.observe(viewLifecycleOwner) { total ->
                binding.dashTvTotalSaldo.text =
                    requireActivity().getString(R.string.total_balance, total.parseLongToMoney())
            }
            listTransaction.observe(viewLifecycleOwner) { listItem ->
                val activity = requireActivity() as MainActivity
                activity.showActionMenu(viewModel.hasSelection())

                dashoardAdapter.submitList(listItem)
            }
        }

        // shared
        sharedViewModel.actionEvent.observe(viewLifecycleOwner){ action ->
            val transactionEntity = viewModel.listTransaction.value
        }
    }

    private fun setupChart() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = DashboardFragment::class.java.simpleName
    }
}