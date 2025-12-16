package com.example.financetrackerapplication.features.aset.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.domain.model.GroupAset
import com.example.financetrackerapplication.domain.repository.AsetRapository
import com.example.financetrackerapplication.domain.usecase.GroupAsetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AsetViewModel @Inject constructor(
    private val groupAsetUseCase: GroupAsetUseCase,
    private val repository: AsetRapository
) : ViewModel() {

    private var parents: List<GroupAset.Parent> = emptyList()

    private val _displayList = MutableLiveData<List<GroupAset>>(emptyList())
    val displayList = _displayList

    private val _order = MutableStateFlow<List<String>>(emptyList())

    init {
        viewModelScope.launch {
            _order
                .flatMapLatest { order ->
                    getListAset(order)
                }
                .collect { list ->
                    // list dari usecase = PARENT LIST
                    parents = list.filterIsInstance<GroupAset.Parent>()
                    _displayList.value = buildDisplayList(parents)
                }
        }
    }


    fun toggleExpand(parentId: String) {
        parents = parents.map {
            if (it.id == parentId)
                it.copy(isExpanded = !it.isExpanded)
            else it
        }

        _displayList.value = buildDisplayList(parents)
    }

    fun toggleSelect(groupAset: GroupAset) {
        when (groupAset) {

            is GroupAset.Parent -> {
                parents = parents.map {
                    if (it.name == groupAset.name)
                        it.copy(isSelected = !it.isSelected)
                    else it
                }
            }

            is GroupAset.Child -> {
                parents = parents.map { p ->
                    p.copy(
                        childAsetList = p.childAsetList.map {
                            if (it.aset.id == groupAset.aset.id)
                                it.copy(isSelected = !it.isSelected)
                            else it
                        }
                    )
                }
            }
        }

        _displayList.value = buildDisplayList(parents)
    }




    private fun getListAset(order: List<String>): Flow<List<GroupAset>> =
        groupAsetUseCase(order)

    fun setOrder(newOrder: List<String>) {
        _order.value = newOrder
    }

    private fun buildDisplayList(data: List<GroupAset.Parent>): List<GroupAset> {
        val result = mutableListOf<GroupAset>()

        for (group in data) {
            result += group
            if (group.isExpanded) {
                result += group.childAsetList
            }
        }

        return result
    }

    fun clearSelection() {
        parents = parents.map { p ->
            p.copy(
                childAsetList = p.childAsetList.map {
                    it.copy(isSelected = false)
                }
            )
        }
        _displayList.value = buildDisplayList(parents)
    }

    fun selectAll() {
        parents = parents.map { p ->
            p.copy(
                childAsetList = p.childAsetList.map {
                    it.copy(isSelected = true)
                }
            )
        }
        _displayList.value = buildDisplayList(parents)
    }

    fun hasSelection(): Boolean {
        return parents.any { parent ->
            parent.childAsetList.any { it.isSelected }
        }
    }

    fun selectedCount(): Int {
        return parents.sumOf { parent ->
            parent.childAsetList.count { it.isSelected }
        }
    }

    fun deleteList(listAset: List<AsetEntity>){
        viewModelScope.launch {
            repository.deleteAset(*listAset.toTypedArray())
        }
    }
}