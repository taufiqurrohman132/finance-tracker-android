package com.example.financetrackerapplication.ui.settings.category.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.financetrackerapplication.data.datasource.local.entity.CategoryEntity
import com.example.financetrackerapplication.domain.model.DataSelected
import com.example.financetrackerapplication.domain.repository.CategoryRapository
import com.example.financetrackerapplication.ui.aset.add.AddAsetActivity.Companion.TAG_ASET_ADD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repoCategory: CategoryRapository
) : ViewModel() {

    private val _selectedIds = MutableLiveData<Set<Long>>(emptySet())
    val selectedIds: LiveData<Set<Long>> = _selectedIds


    @OptIn(ExperimentalCoroutinesApi::class)
    val listCategory: LiveData<List<DataSelected<CategoryEntity>>> =
        selectedIds.asFlow()
            .flatMapLatest { ids ->
                repoCategory.getAllCategory().map { list ->
                    list.map { DataSelected(it, ids.contains(it.id)) }
                }
            }.asLiveData()


    fun saveCategory(
        name: String,
        categoryType: String,
        iconName: String?
    ) {
        val category = CategoryEntity(
            name = name,
            iconName = iconName,
            categoryType = categoryType
        )
        viewModelScope.launch {
            Log.d(TAG_ASET_ADD, "saveCategoy: category viewmodel a = $category")
            repoCategory.insertCategory(category)
        }
    }
    fun updateCategory(
        name: String,
        categoryType: String,
        iconName: String?
    ) {
        val category = CategoryEntity(
            name = name,
            iconName = iconName,
            categoryType = categoryType
        )
        viewModelScope.launch {
            Log.d(TAG_ASET_ADD, "updateCategoy: category viewmodel a = $category")
            repoCategory.updateCategory(category)
        }
    }

    fun getCategory(id: Long): StateFlow<CategoryEntity?> {
        return repoCategory.getCategory(id)
            .stateIn(viewModelScope, SharingStarted.Lazily, null)
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
        Log.d(TAG_ASET_ADD, "hasSelection: selected id = ${selectedIds.value}")
        return selectedIds.value?.isNotEmpty() ?: false
    }

    fun clearSelection() {
        _selectedIds.value = emptySet()
    }

    fun selectAll() {
        viewModelScope.launch {
            val allIds = repoCategory.getAllCategory()
                .first()
                .map { it.id }

            _selectedIds.value = allIds.toSet()
        }
    }

    fun selectedCount(): Int {
        return selectedIds.value?.size ?: 0
    }

    fun deleteList(list: List<CategoryEntity>) {
        viewModelScope.launch {
            _selectedIds.value = emptySet()
            repoCategory.deleteCategory(*list.toTypedArray())
        }
    }

}