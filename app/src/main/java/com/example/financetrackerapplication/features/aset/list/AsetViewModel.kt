package com.example.financetrackerapplication.features.aset.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.financetrackerapplication.domain.model.ChildAset
import com.example.financetrackerapplication.domain.model.GroupAset
import com.example.financetrackerapplication.domain.repository.AsetRapository
import com.example.financetrackerapplication.domain.usecase.GroupAsetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AsetViewModel @Inject constructor(
    private val groupAsetUseCase: GroupAsetUseCase,
    private val repository: AsetRapository
): ViewModel() {

//    val listAset: LiveData<List<AsetEntity>> =
//        repository.getAset().asLiveData()

    private val _childAsetList = MutableStateFlow<List<GroupAset>>(emptyList())
    val childAsetList = _childAsetList.asStateFlow()

    private val _order = MutableStateFlow<List<String>>(emptyList())

    init {
        viewModelScope.launch {
            _order
                .flatMapLatest { order ->
                    getListAset(order)
                }
                .collect { list ->
                    _childAsetList.value = list
                }
        }
    }

    fun toggleSelect(childAset: ChildAset){
        val groups = _childAsetList.value.toMutableList()

        // cari group yang punya child ini
        for (gIndex in groups.indices) {
            val group = groups[gIndex]

            val cIndex = group.asetList.indexOf(childAset)
            if (cIndex != -1) {
                // update child
                val newChild = childAset.copy(isSelected = !childAset.isSelected)

                val newChildList = group.asetList.toMutableList()
                newChildList[cIndex] = newChild

                // update group
                groups[gIndex] = group.copy(asetList = newChildList)

                // push state ke flow
                _childAsetList.value = groups
                return
            }
        }  }

    private fun getListAset(order: List<String>): Flow<List<GroupAset>>  =
        groupAsetUseCase(order)

    fun setOrder(newOrder: List<String>) {
        _order.value = newOrder
    }
}