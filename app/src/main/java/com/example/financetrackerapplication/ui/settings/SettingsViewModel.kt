package com.example.financetrackerapplication.ui.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetrackerapplication.domain.model.UserStatus
import com.example.financetrackerapplication.domain.repository.AuthRepository
import com.example.financetrackerapplication.domain.usecase.GetUserStatusUseCase
import com.example.financetrackerapplication.domain.usecase.SignInAnonymouslyUseCase
import com.example.financetrackerapplication.domain.usecase.SignInWithGoogleUseCase
import com.example.financetrackerapplication.domain.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
) : ViewModel() {
    private val _userStatus = MutableLiveData<UserStatus>()
    val userStatus: LiveData<UserStatus> get() = _userStatus

    fun loginGuest() {
        viewModelScope.launch {
            val result = signInAnonymouslyUseCase()
            if (result.isSuccess) {
                Log.d("SettingsVM", "Login guest berhasil")
            } else {
                Log.e("SettingsVM", "Login guest gagal: ${result.exceptionOrNull()?.message}")
            }
            loadUserStatus()
        }
    }

    fun signInWithGoogle(idToken: String){
        viewModelScope.launch {
            val result = signInWithGoogleUseCase(idToken)
            if (result.isSuccess) {
                Log.d("AuthVM", "Login Google sukses")
            } else {
                Log.e("AuthVM", "Login Google gagal: ${result.exceptionOrNull()?.message}")
            }
            loadUserStatus()
        }
    }


    fun loadUserStatus() {
        viewModelScope.launch {
            _userStatus.value = getUserStatusUseCase()
            Log.d("SettingsVM", "Load user status... ${_userStatus.value}")
        }
    }

    fun logout() {
        viewModelScope.launch {
            Log.d("SettingsVM", "Tombol logout ditekan")
            signOutUseCase() // log out
            loadUserStatus()
        }
    }


}