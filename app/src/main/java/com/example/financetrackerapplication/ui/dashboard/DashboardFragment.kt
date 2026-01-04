package com.example.financetrackerapplication.ui.dashboard

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.azikar24.monthyearpicker.MonthYearPicker
import com.example.financetrackerapplication.Action
import com.example.financetrackerapplication.MainActivity
import com.example.financetrackerapplication.MainSharedViewModel
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.databinding.FragmentDashboardBinding
import com.example.financetrackerapplication.domain.model.ItemTransaction
import com.example.financetrackerapplication.ui.transaction.TransactionActivity
import com.example.financetrackerapplication.utils.Extention.parseLongToMoneyShort
import com.example.financetrackerapplication.utils.Navigation
import com.example.financetrackerapplication.utils.TimeUtils
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var dashoardAdapter: DashboardAdapter
    private val viewModel: DashboardViewModel by viewModels()
    private val sharedViewModel: MainSharedViewModel by activityViewModels()

    private var currentList: List<ItemTransaction> = emptyList()

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

        init()
        setupRecyclerView()
        setupListener()
        observer()
        setupChart()
    }

    private fun init() {
        val arrayMonth = requireActivity().resources.getStringArray(R.array.month_short_id)
        val year = LocalDate.now().year
        val month = LocalDate.now().month.value
        binding.dashBtnFilterMounth.text = "${arrayMonth[month - 1]} $year"


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
                viewModel.toggleSelect(item.id ?: 0L)

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
            btnPilihSemuaSelection.setOnClickListener { viewModel.selectAll(currentList) }

            dashBtnFilterMounth.setOnClickListener { showMonthYearPickers(dashBtnFilterMounth.text.toString()) }
        }
    }

    private fun observer() {
        viewModel.apply {
            // header total
            listTransaction.observe(viewLifecycleOwner) { listItem ->
                currentList = listItem

                val activity = requireActivity() as MainActivity
                activity.showActionMenu(viewModel.hasSelection(), R.id.navigation_dashboard)

                binding.apply {
                    dashSelectionLayout.isVisible = viewModel.hasSelection()
                    toolbar.apply {
                        title =
                            if (!viewModel.hasSelection()) getString(R.string.transaksi) else null
                        menu.apply {
                            findItem(R.id.dash_search)?.isVisible = !viewModel.hasSelection()
                            findItem(R.id.dash_bookmark)?.isVisible = !viewModel.hasSelection()
                        }
                    }
                }
                dashoardAdapter.submitList(listItem)

                val totalIncome = listItem.sumOf { it.income ?: 0L }
                binding.dashTvIncomeTotal.text = totalIncome.parseLongToMoneyShort()

                binding.dashTvTotalSaldo.text =
                    requireActivity().getString(
                        R.string.total_balance,
                        totalBalance(listItem).parseLongToMoneyShort()
                    )
            }

            // barchart
            dailyIncome.observe(viewLifecycleOwner) { dailyTotal ->
                val dataSet = lineDataSet(dailyTotal)
                binding.dashChartIncome.apply {
                    data = LineData(dataSet)
                    notifyDataSetChanged()
                    invalidate()
                }

                totalPercentage.observe(viewLifecycleOwner) { presenteage ->
                    Log.d(TAG, "observer: total presentage = $presenteage")
                    val textPresentage = String.format(Locale.getDefault(), "%+.2f%%", presenteage)
                    binding.dashTotalPresentage.text = textPresentage
                }
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

    }

    private fun lineDataSet(dailyTotal: LongArray): LineDataSet {
        val entries = dailyTotal.mapIndexed { index, total ->
            Entry(
                (index + 1).toFloat(),   // X = hari
                total.toFloat()          // Y = total income hari itu
            )
        }
        Log.d(TAG, "observer: daily income = $entries")
        val dataSet = LineDataSet(entries, "Income").apply {
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

            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "${value.toInt()} M"
                }
            }
        }
        return dataSet
    }

    private fun totalBalance(list: List<ItemTransaction>): Long {
        val totalIncome = list.sumOf { it.income ?: 0L }
        val totalExpense = list.sumOf { it.expense ?: 0L }

        return totalIncome - totalExpense
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

    private fun showMonthYearPickers(textMonthYear: String) {
        val (year, month) = TimeUtils.parseMonthYear(requireContext(), textMonthYear)
        val arrayMonth = requireActivity().resources.getStringArray(R.array.month_short_id)

        val localDate = LocalDate.of(year, month, 1)
        val date = Date.from(
            localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        )

        val minDate = Calendar.getInstance().apply {
            add(Calendar.YEAR, -20)
        }.time

        // Max date = 120 bulan ke depan (10 tahun)
        val maxDate = Calendar.getInstance().apply {
            add(Calendar.YEAR, 20)
        }.time

        val safeDate = when {
            date.before(minDate) -> minDate
            date.after(maxDate) -> maxDate
            else -> date
        }
        Log.d(TAG, "showMonthYearPickers: month = ${month.value}")
        val monthYearPickerBuilder = MonthYearPicker.Builder(requireActivity())
            .setTitle("Select Month and Year")
            .setLifeCycleOwner(viewLifecycleOwner)
            .setStyle(R.style.ThemeMonthYearPicker)
            .setLocale(Locale.getDefault())
            .setMinDate(minDate)
            .setMaxDate(maxDate)
            .setSelectedDate(safeDate)
            .setShortMonths(true)
            .setCancelable(false)
            .setCallback { selectedYear, selectedMonth ->
                viewModel.setMonthYear(Month.of(selectedMonth + 1), selectedYear)
                binding.dashBtnFilterMounth.text = "${arrayMonth[selectedMonth]} $selectedYear"
            }
        monthYearPickerBuilder.build().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = DashboardFragment::class.java.simpleName
    }
}