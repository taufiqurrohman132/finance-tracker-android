package com.example.financetrackerapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainSharedViewModel @Inject constructor() : ViewModel() {
    private val _actionEvent = MutableLiveData<Action>()
    val actionEvent: LiveData<Action> = _actionEvent

    fun onBottomAction(action: Action){
        _actionEvent.value = action
    }
}

enum class Action{
    DELETE, PIN
}