package com.example.financetrackerapplication.features.dashboard

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
import com.example.financetrackerapplication.features.transaction.TransactionActivity
import com.example.financetrackerapplication.utils.Extention.parseLongToMoney
import com.example.financetrackerapplication.utils.Navigation
import com.example.financetrackerapplication.utils.TimeUtils
import com.github.mikephil.charting.components.XAxis
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

            dashBtnFilterMounth.setOnClickListener { showMonthYearPickers(dashBtnFilterMounth.text.toString()) }
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

//    private fun showMonthYearPicker(textMonthYear: String) {
//        val (year, month) = TimeUtils.parseMonthYear(requireContext(), textMonthYear)
//        val arrayMonth = requireActivity().resources.getStringArray(R.array.month_short_id)
//
//        val dialog = MonthYearPickerDialog.Builder(
//            context = requireContext(),
//            themeResId = R.style.MonthYearPickerRounded,
//            onDateSetListener = { selectedYear, selectedMonth ->
//                // year + month
//                Log.d(TAG, "showMonthYearPicker: montch = $month, year = $year")
//                viewModel.setMonthYear(Month.of(selectedMonth + 1), selectedYear)
//                binding.dashBtnFilterMounth.text = "${arrayMonth[selectedMonth]} $selectedYear"
//            },
//            selectedMonth = month.value - 1,
//            selectedYear = year
//        )
//            .setNegativeButton(R.string.batal)
//            .setPositiveButton(R.string.oke)
//            .build()
//
//        val view = CustomDialogBinding.inflate(
//            LayoutInflater.from(requireContext())
//        )
//        dialog.apply {
////            setView(
////                view.root,
////                20,
////                20,
////                20,
////                20
////            )
//            setTitle("Select Mount And Year")
//            show()
//        }
//    }

    //    private fun showMonthYearPickers(textMonthYear: String) {
//        val (year, month) = TimeUtils.parseMonthYear(requireContext(), textMonthYear)
//        val arrayMonth = requireActivity().resources.getStringArray(R.array.month_short_id)
//
//        val dateMillis = LocalDate.of(year, month, 1)
//            .atStartOfDay(ZoneId.systemDefault())
//            .toInstant()
//            .toEpochMilli()
//        val calendar = Calendar.getInstance().apply {
//            timeInMillis = dateMillis
//        }
//
//        val dialog = YearMonthPickerDialog(
//            requireContext(),
//            { selectedYear, selectedMonth ->
//                viewModel.setMonthYear(Month.of(selectedMonth + 1), selectedYear)
//                binding.dashBtnFilterMounth.text = "${arrayMonth[selectedMonth]} $selectedYear"
//            },
//            R.style.MyDialogTheme,
//            Color.BLACK,
//            calendar,
//        )
//
//        dialog.apply {
//
//            show()
//        }
//    }
//    private fun showMonthYearPickers(textMonthYear: String) {
//        val (year, month) = TimeUtils.parseMonthYear(requireContext(), textMonthYear)
//        val arrayMonth = requireActivity().resources.getStringArray(R.array.month_short_id)
//
//        Log.d(TAG, "showMonthYearPickers: month = ${month.value}")
//        val dialog = RackMonthPicker(requireContext())
//        dialog.apply {
//            setLocale(Locale.getDefault())
//            setSelectedMonth(month.value -1)
//            setSelectedYear(year)
//            requireContext().resources.apply {
//                setNegativeText(getString(R.string.batal))
//                setPositiveText(getString(R.string.oke))
//                setColorTheme(R.color.color_primary)
//            }
//            setPositiveButton { selectedMonth, startDate, endDate, selectedYear, monthLabel ->
//                viewModel.setMonthYear(Month.of(selectedMonth), selectedYear)
//                binding.dashBtnFilterMounth.text = "${arrayMonth[selectedMonth -1]} $selectedYear"
//                dialog.dismiss()
//            }
//            setNegativeButton {
//                dialog.dismiss()
//            }
//            show()
//
//        }
//    }
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