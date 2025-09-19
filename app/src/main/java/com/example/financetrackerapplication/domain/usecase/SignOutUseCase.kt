package com.example.financetrackerapplication.domain.usecase

import com.example.financetrackerapplication.domain.repository.AuthRepository

class SignOutUseCase(private val repo: AuthRepository) {
    operator fun invoke(){
        repo.signOut()
    }
}