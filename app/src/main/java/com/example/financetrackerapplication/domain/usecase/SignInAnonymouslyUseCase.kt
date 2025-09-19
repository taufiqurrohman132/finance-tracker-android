package com.example.financetrackerapplication.domain.usecase

import android.util.Log
import com.example.financetrackerapplication.domain.repository.AuthRepository

class SignInAnonymouslyUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(): Result<Unit> {
        Log.d("SignInAnonUseCase",   "Menjalankan use case login anonymous")
        return repo.signInAnonymously()
    }
}