package com.example.financetrackerapplication.features.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.domain.model.ItemTransaction
import com.example.financetrackerapplication.domain.repository.TransactionRepository
import com.example.financetrackerapplication.domain.usecase.GroupTransactionsUseCase
import com.example.financetrackerapplication.utils.TimeUtils
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: TransactionRepository,
    private val groupTransactionsUseCase: GroupTransactionsUseCase,
) : ViewModel() {
    private val _displayBarChart = MutableLiveData<List<BarEntry>>()
    val displayBarChart: LiveData<List<BarEntry>> = _displayBarChart

    private var transaction: List<ItemTransaction> = emptyList()

    private val _listTransaction = MutableLiveData<List<ItemTransaction>>()
    val listTransaction: LiveData<List<ItemTransaction>> = _listTransaction

    init {
        viewModelScope.launch {
            repository.getAllTransaction().collect { list ->
                transaction = groupTransactionsUseCase.execute(
                    list,
                    LocalDate.now().month,
                    LocalDate.now().year
                )
                _listTransaction.value = transaction
            }
        }
    }

    fun toggleSelect(itemTransaction: ItemTransaction) {
        transaction = transaction.map { trans ->
            if (trans.id == itemTransaction.id)
                trans.copy(isSelected = !trans.isSelected)
            else trans
        }
        _listTransaction.value = transaction
    }

    fun hasSelection(): Boolean {
        return transaction.any { trans ->
            trans.isSelected
        }
    }

    fun clearSelection() {
        transaction = transaction.map {
            it.copy(isSelected = false)
        }
        _listTransaction.value = transaction
    }

    fun selectAll() {
        transaction = transaction.map {
            it.copy(isSelected = true)
        }
        _listTransaction.value = transaction
    }

    fun deleteList(listAset: List<TransactionEntity>) {
        viewModelScope.launch {
            repository.deleteTransaction(*listAset.toTypedArray())
        }
    }

    fun setBarCharInMonth(year: Int, month: Int) {
        val cal = Calendar.getInstance()
        val daysInMonth = TimeUtils.daysInMonth(year, month)

        val dailyTotal = LongArray(daysInMonth)

        listTransaction.value?.forEach { transaction ->
            cal.timeInMillis = transaction.dataItem?.transaction?.dateTimeMillis ?: 0L
            if (
                cal.get(Calendar.YEAR) == year &&
                cal.get(Calendar.MONTH) == month &&
                (transaction.dataItem?.transaction?.type) == TransactionEntity.TYPE_INCOME
            ) {
                val day = cal.get(Calendar.DAY_OF_MONTH) // 1..31
                dailyTotal[day - 1] += transaction.dataItem.transaction.amount
            }
        }

        val maxValue = listTransaction.value?.maxOf { it.dataItem?.transaction?.amount ?: 0L } ?: 0L

        val entries = dailyTotal.mapIndexed { index, total ->
            Log.d(TAG, "x=$index, y=$total")
            val percentation = (total.toFloat() / maxValue) * 100f
            BarEntry((index + 1).toFloat(), percentation)
        }
        Log.d(TAG, "total entries = ${entries.size}")




        _displayBarChart.value = entries
    }

    fun setMonthYear(month: Month, year: Int){
        viewModelScope.launch {
            repository.getAllTransaction().collect { list ->
                transaction = groupTransactionsUseCase.execute(
                    list,
                    month,
                    year
                )
                _listTransaction.value = transaction
            }
        }
    }

    companion object {
        //        val CAP = 10_000_000f // misal versi miliar
        private val TAG = DashboardViewModel::class.java.simpleName
    }
}