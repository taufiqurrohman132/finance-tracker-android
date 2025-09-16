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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _userStatus = MutableLiveData<UserStatus>()
    val userStatus: LiveData<UserStatus> get() = _userStatus

    init {
        viewModelScope.launch {
            Log.d("SettingsVM", "Tombol login guest ditekan")
            val result = signInAnonymouslyUseCase()
            if (result.isSuccess){
                Log.d("SettingsVM", "Login guest berhasil")
            }else {
                Log.e("SettingsVM", "Login guest gagal: ${result.exceptionOrNull()?.message}")
            }
            loadUserStatus()
        }
    }

    fun loadUserStatus(){
        viewModelScope.launch {
            Log.d("SettingsVM", "Load user status...")
            _userStatus.value = getUserStatusUseCase()
        }
    }

    fun logout() {
        viewModelScope.launch {
            Log.d("SettingsVM", "Tombol logout ditekan")
            authRepository.signOut()
            loadUserStatus()
        }
    }


}