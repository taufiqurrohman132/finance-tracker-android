package com.example.financetrackerapplication.features.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.financetrackerapplication.data.datasource.local.entity.TransactionEntity
import com.example.financetrackerapplication.domain.model.DailyIncome
import com.example.financetrackerapplication.domain.model.ItemTransaction
import com.example.financetrackerapplication.domain.repository.TransactionRepository
import com.example.financetrackerapplication.domain.usecase.GroupTransactionsUseCase
import com.example.financetrackerapplication.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: TransactionRepository,
    private val groupTransactionsUseCase: GroupTransactionsUseCase,
) : ViewModel() {

    private val _selectedYearMonth = MutableLiveData<Pair<Int, Int>>() // Pair<year, month>
    private val selectedYearMonth: LiveData<Pair<Int, Int>> = _selectedYearMonth

    private val _selectedIds = MutableLiveData<Set<Long>>(emptySet())
    val selectedIds: LiveData<Set<Long>> = _selectedIds

    private val allTransaction = repository.getAllTransaction()

    val listTransaction: LiveData<List<ItemTransaction>> =
        selectedYearMonth.switchMap { (year, month) ->
            selectedIds.switchMap { ids ->
                liveData {
                    allTransaction.collect { list ->
                        emit(
                            groupTransactionsUseCase.execute(
                                list,
                                Month.of(month),
                                year
                            ).map {
                                it.copy(isSelected = ids.contains(it.id))
                            }
                        )
                    }
                }
            }
        }

    val dailyIncome: LiveData<LongArray> =
        selectedYearMonth.switchMap { (year, month) ->
            liveData {
                repository.getDailyIncome(
                    year.toString(),
                    month.toString().padStart(2, '0'),
                    TransactionEntity.TYPE_INCOME
                ).collect { list ->
                    emit(setTotalDailyInMonth(year, month, list))
                }
            }
        }

    val dailyExpanse: LiveData<LongArray> =
        selectedYearMonth.switchMap { (year, month) ->
            liveData {
                repository.getDailyIncome(
                    year.toString(),
                    month.toString().padStart(2, '0'),
                    TransactionEntity.TYPE_EXPANSE
                ).collect { list ->

                    emit(setTotalDailyInMonth(year, month, list))
                }
            }
        }

    val totalPercentage = run {
        val (todayStart, todayEnd) = TimeUtils.getDayRange(0)
        val (yesterdayStart, yesterdayEnd) = TimeUtils.getDayRange(1)

        repository.getTotalByDay(todayStart, todayEnd)
            .combine(
                repository.getTotalByDay(yesterdayStart, yesterdayEnd)
            ){ todayTotal, yesterdayTotal->
                if (yesterdayTotal == 0L) {
                    100.0
                } else {
                    ((todayTotal - yesterdayTotal).toDouble() / yesterdayTotal) * 100
                }
            }.asLiveData()
    }


    init {
        val now = LocalDate.now()
        _selectedYearMonth.value = now.year to now.monthValue
    }

    fun toggleSelect(id: Long) {
        val current = _selectedIds.value.orEmpty().toMutableSet()
        if (current.contains(id)) {
            current.remove(id)
        } else {
            current.add(id)
        }
        _selectedIds.value = current
    }


    fun hasSelection(): Boolean {
        Log.d(TAG, "hasSelection: selected id = ${selectedIds.value}")
        return selectedIds.value?.isNotEmpty() ?: false
    }

    fun clearSelection() {
        val current = _selectedIds.value.orEmpty().toMutableSet()
        current.clear()
        _selectedIds.value = current
    }

    fun selectAll(currentList: List<ItemTransaction>) {
        _selectedIds.value = currentList
            .mapNotNull { it.id }
            .toSet()
    }

    fun deleteList(listAset: List<TransactionEntity>) {
        viewModelScope.launch {
            _selectedIds.value = emptySet()
            repository.deleteTransaction(*listAset.toTypedArray())
        }
    }

    private fun setTotalDailyInMonth(year: Int, month: Int, list: List<DailyIncome>): LongArray {
        val daysInMonth = TimeUtils.daysInMonth(year, month)

        val dailyTotal = LongArray(daysInMonth)

        list.forEach {
            dailyTotal[it.day - 1] = it.total
        }

        return dailyTotal
    }

    fun setMonthYear(month: Month, year: Int) {
        _selectedYearMonth.value = year to month.value
    }

    companion object {
        //        val CAP = 10_000_000f // misal versi miliar
        private val TAG = DashboardViewModel::class.java.simpleName
    }
}