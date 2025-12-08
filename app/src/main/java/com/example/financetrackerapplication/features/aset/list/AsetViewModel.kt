package com.example.financetrackerapplication.features.aset.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.financetrackerapplication.domain.model.ItemAset2
import com.example.financetrackerapplication.domain.repository.AsetRapository
import com.example.financetrackerapplication.domain.usecase.GroupAsetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AsetViewModel @Inject constructor(
    private val groupAsetUseCase: GroupAsetUseCase,
    private val repository: AsetRapository
): ViewModel() {

//    val listAset: LiveData<List<AsetEntity>> =
//        repository.getAset().asLiveData()

    fun getListAset(order: List<String>): LiveData<List<ItemAset2>>  = liveData {
        repository.getAset().collect{ listAset ->
            emit(groupAsetUseCase.execute(listAset, order))
        }
    }

}