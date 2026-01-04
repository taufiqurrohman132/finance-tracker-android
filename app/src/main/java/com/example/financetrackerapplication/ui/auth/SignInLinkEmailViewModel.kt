package com.example.financetrackerapplication.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetrackerapplication.domain.usecase.SignInWithLinkEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInLinkEmailViewModel @Inject constructor(
    private val signInWithLinkEmailUseCase: SignInWithLinkEmailUseCase
) : ViewModel() {
    fun signInLinkEmail(email: String) {
        viewModelScope.launch {
            signInWithLinkEmailUseCase(email)
        }
    }
}