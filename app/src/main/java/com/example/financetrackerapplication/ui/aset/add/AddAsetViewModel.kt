package com.example.financetrackerapplication.features.aset.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetrackerapplication.data.datasource.local.entity.AsetEntity
import com.example.financetrackerapplication.domain.repository.AsetRapository
import com.example.financetrackerapplication.features.aset.add.AddAsetActivity.Companion.TAG_ASET_ADD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAsetViewModel @Inject constructor(
    private val repository: AsetRapository
) : ViewModel() {

    fun saveAset(
        name: String,
        initialBalance: Long,
        groupAset: String,
        iconName: String?
    ){
        val aset = AsetEntity(
            name = name,
            initialBalance = initialBalance,
            iconName = iconName,
            groupAset = groupAset
        )
        viewModelScope.launch {
            Log.d(TAG_ASET_ADD, "saveAset: aset viewmodel a = $aset")
            repository.insertAset(aset)
        }
    }
}