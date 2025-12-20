package com.example.financetrackerapplication.features.dashboard

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.financetrackerapplication.Action
import com.example.financetrackerapplication.MainActivity
import com.example.financetrackerapplication.MainSharedViewModel
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.databinding.FragmentDashboardBinding
import com.example.financetrackerapplication.features.transaction.TransactionActivity
import com.example.financetrackerapplication.utils.Extention.parseLongToMoney
import com.example.financetrackerapplication.utils.Navigation
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

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
        setupChart()
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
        binding.apply {
            dashFabAddTrans.setOnClickListener {
                val intent = Intent(requireActivity(), TransactionActivity::class.java)
                startActivity(intent)
            }
            btnBatalkanSelection.setOnClickListener { viewModel.clearSelection() }
            btnPilihSemuaSelection.setOnClickListener { viewModel.selectAll() }
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
                activity.showActionMenu(viewModel.hasSelection(), R.id.navigation_dashboard)

                binding.dashSelectionLayout.isVisible = viewModel.hasSelection()
                dashoardAdapter.submitList(listItem)

                if (!listItem.isNullOrEmpty()) {
                    viewModel.setBarCharInMonth(2025, Calendar.DECEMBER)
                }

                val totalIncome = listItem.sumOf { it.income ?: 0L }
                binding.dashTvIncomeTotal.text = totalIncome.parseLongToMoney()
            }
        }

        // shared
        sharedViewModel.actionEvent.observe(viewLifecycleOwner) { action ->
            val transactionEntity = viewModel.listTransaction.value
                ?.filter { it.isSelected }
                ?.mapNotNull { it.dataItem?.transaction }
                ?: emptyList()

            when (action) {
                Action.DELETE -> {
                    viewModel.deleteList(transactionEntity)
                }

                Action.PIN -> {

                }
            }
        }

        // barchart
        viewModel.displayBarChart.observe(viewLifecycleOwner) { barEntriesList ->
            val dataSet = LineDataSet(barEntriesList, "Income").apply {
                setDrawValues(false)
                // agar melengkung
                mode = LineDataSet.Mode.CUBIC_BEZIER
                cubicIntensity = 0.15f

                // agar titik hilang
                setDrawCircles(false)
                setDrawFilled(true)
                val gradient = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(
                        Color.GREEN,
                        Color.TRANSPARENT
                    )
                )
                fillDrawable = gradient

//                lineWidth = 2f

                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "${value.toInt()} M"
                    }
                }
            }
            binding.dashChartIncome.apply {

                data = LineData(dataSet)
//                axisLeft.resetAxisMinimum()
                notifyDataSetChanged()
                invalidate()
            }
        }
    }

    private fun setupChart() {
        binding.dashChartIncome.apply {
            description.isEnabled = false
            legend.isEnabled = false

            isAutoScaleMinMaxEnabled = false

            // MATIKAN semua auto offset
            setViewPortOffsets(0f, 0f, 0f, 32f)

            // Axis
            axisRight.isEnabled = false
            axisLeft.apply {
                isEnabled = false
                spaceTop = 0f
                spaceBottom = 0f
            }

            xAxis.apply {
                granularity = 1f
                setLabelCount(7, false)
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                textColor = Color.WHITE
            }

            invalidate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = DashboardFragment::class.java.simpleName
    }
}