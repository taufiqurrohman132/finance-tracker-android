package com.example.financetrackerapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.financetrackerapplication.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainSharedViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _actionEvent = MutableLiveData<Action>()
    val actionEvent: LiveData<Action> = _actionEvent

    val themeMode: LiveData<Int> =
        settingsRepository.themeModeFlow.asLiveData()

    fun onBottomAction(action: Action){
        _actionEvent.value = action
    }



}

enum class Action{
    DELETE, PIN
}