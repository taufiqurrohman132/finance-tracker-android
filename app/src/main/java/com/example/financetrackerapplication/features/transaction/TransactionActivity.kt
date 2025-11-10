package com.example.financetrackerapplication.features.transaction

import android.os.Bundle
import android.text.format.DateFormat
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.data.datasource.local.entity.CategoryEntity
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.databinding.ActivityTransactionBinding
import com.example.financetrackerapplication.databinding.CustomDatePickerBinding
import com.example.financetrackerapplication.databinding.CustomTimePickerBinding
import com.example.financetrackerapplication.databinding.SheetAddTransCategoryBinding
import com.example.financetrackerapplication.domain.model.TransOptions
import com.example.financetrackerapplication.utils.DialogUtils
import com.example.financetrackerapplication.utils.Extention.focus
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionBinding

    private val viewModel: TransactionViewModel by viewModels()

    // adapter
    private lateinit var asetAdapter: OptionsAdapter<AsetEntity>
    private lateinit var categoryAdapter: OptionsAdapter<CategoryEntity>

    // input selected
    private var asetSelectedId: Long = -1
    private var categorySelectedId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setupRecyclerView()
        setupListener()
    }

    // inisialize ui / setup ui pertama ketika baru tampil
    private fun init() {
        updateDate()
        updateTime()
    }

    private fun setupListener() {
        binding.apply {
            addEtKategori.setOnClickListener {
                showBottomSheetDialog { dialog ->
                    val adapter = OptionsAdapter<CategoryEntity> {
                        categorySelectedId = it.id
                        binding.addEtKategori.setText(it.name)
                        dialog.dismiss()
                    }
                    lifecycleScope.launch {
                        val data = viewModel.getAllCategory()
                        adapter.submitList(data)
                    }
                    adapter
                }
            }
            addEtAset.setOnClickListener {
                showBottomSheetDialog { dialog ->
                    val adapter = OptionsAdapter<AsetEntity> {
                        asetSelectedId = it.id
                        binding.addEtAset.setText(it.name)
                        dialog.dismiss()
                    }
                    lifecycleScope.launch {
                        val data = viewModel.getAllAset()
                        adapter.submitList(data)
                    }
                    adapter
                }
            }

            addEtTime.setOnClickListener { showTimePicker(addEtTime.text.toString()) }
            addEtDate.setOnClickListener { showDatePicker(addEtDate.text.toString()) }
            addBtnSave.setOnClickListener { saveTransaction() }
        }
    }

    private fun setupRecyclerView() {
        // aset


    }

    private fun saveTransaction() {
        binding.apply {
            viewModel.insertTransaction(
                amount = addEtTotal.text.toString().toDouble(),
                type = if (buttonGroupTypeTransaction.position == 0) TransactionEntity.TYPE_INCOME
                else TransactionEntity.TYPE_EXPANSE,
                dateTimeMillis = System.currentTimeMillis(),
                description = addEtDeskripsi.text.toString(),
                accountId = asetSelectedId,
                categoryId = categorySelectedId
            )
        }
    }

    private fun observer() {
        viewModel.apply {

        }
    }

    private fun <T : TransOptions> showBottomSheetDialog(optionsAdapter: (BottomSheetDialog) -> OptionsAdapter<T>) {
        val bindingBottomSheet = SheetAddTransCategoryBinding.inflate(layoutInflater)
        val dialog = DialogUtils.showBottomSheet(this, bindingBottomSheet.root)

        bindingBottomSheet.apply {
            transRvListCategoryOptions.apply {
                adapter = optionsAdapter.invoke(dialog)
                layoutManager = GridLayoutManager(this@TransactionActivity, 3)
            }
        }
    }

    // set date
    private fun showDatePicker(textDate: String) {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val parseDate = dateFormat.parse(textDate)

        // Create a Calendar instance to get the current date
        val calendar = Calendar.getInstance().apply {
            time = parseDate!!
        }

        // menentukan date awal
        val viewDate = CustomDatePickerBinding.inflate(layoutInflater)
        viewDate.datePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            null
        )

        // dialog
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(viewDate.root)
            .setPositiveButton("OK") { _, _ ->
                val year = viewDate.datePicker.year
                val month = viewDate.datePicker.month
                val day = viewDate.datePicker.dayOfMonth

                val selected = calendar.apply {
                    set(year, month, day)
                }
                updateDate(selected)
            }
            .setNegativeButton("Batal", null)
            .create()

        dialog.show()
    }

    // update tanggal
    private fun updateDate(calendar: Calendar? = null) {
        val formatter = "dd-MM-yyyy"
        val simpleDateFormat = SimpleDateFormat(formatter, Locale.getDefault())
        val date = calendar?.let {
            simpleDateFormat.format(calendar.time)
        } ?: simpleDateFormat.format(Calendar.getInstance().time)
        binding.addEtDate.setText(date)
        binding.addEtTotal.focus(this)

    }

    private fun showTimePicker(timeString: String){
        val timeFormatter = SimpleDateFormat("hh:mm", Locale.getDefault())
        val parseTime = timeFormatter.parse(timeString) // konversi

        val timeBinding = CustomTimePickerBinding.inflate(layoutInflater)
        val c = Calendar.getInstance().apply {
            time = parseTime!! // set time di calender
        }

        // set picker from calendar
        timeBinding.timePicker.apply {
            setIs24HourView(DateFormat.is24HourFormat(this@TransactionActivity))
            hour = c.get(Calendar.HOUR_OF_DAY)
            minute = c.get(Calendar.MINUTE)
        }

        MaterialAlertDialogBuilder(this)
            .setView(timeBinding.root)
            .setPositiveButton("OK") { _, _ ->
                val hour = timeBinding.timePicker.hour
                val minute = timeBinding.timePicker.minute

                // set calendar dari picker
                val selected = c.apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }
                updateTime(selected)
            }.setNegativeButton("Batal", null)
            .show()
    }

    private fun updateTime(calendar: Calendar? = null){
        val formatter = SimpleDateFormat("hh:mm", Locale.getDefault())
        val time = calendar?.let {
            formatter.format(it.time)
        } ?: formatter.format(Calendar.getInstance().time)
        binding.addEtTime.setText(time)
        binding.addEtTotal.focus(this)
    }
}