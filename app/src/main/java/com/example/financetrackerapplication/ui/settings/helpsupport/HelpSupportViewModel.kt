package com.example.financetrackerapplication.ui.settings.helpsupport

import androidx.lifecycle.ViewModel
import com.example.financetrackerapplication.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HelpSupportViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

}