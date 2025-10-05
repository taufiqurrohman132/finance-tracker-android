package com.example.financetrackerapplication.domain.usecase

import android.util.Log
import com.example.financetrackerapplication.domain.model.UserStatus
import com.example.financetrackerapplication.domain.repository.AuthRepository

class GetUserStatusUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(): UserStatus {
        Log.d("GetUserStatusUseCase", "Ambil status user...")
        return repo.getUserStatus()
//        when {
//            !repo.isUserLoggedIn() -> {
//                Log.d("GetUserStatusUseCase", "Status: LoggedOut")
//                UserStatus.LoggedOut
//            }
//            repo.isAnonymousUser() -> {
//                Log.d("GetUserStatusUseCase", "Status: Guest")
//                UserStatus.Anonymous
//            }
//            else -> {
//                Log.d("GetUserStatusUseCase", "Status: LoggedIn")
//                UserStatus.LoggedIn
//            }
//        }
    }
}
