package com.example.financetrackerapplication.features.settings.categorymanage.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.data.datasource.local.entity.CategoryEntity
import com.example.financetrackerapplication.domain.repository.CategoryRapository
import com.example.financetrackerapplication.features.aset.add.AddAsetActivity.Companion.TAG_ASET_ADD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repoCategory: CategoryRapository
) : ViewModel(){

    fun saveCategory(
        name: String,
        categoryType: String,
        iconName: String?
    ){
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


}