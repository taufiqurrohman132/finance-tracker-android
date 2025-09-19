package com.example.financetrackerapplication.domain.usecase

import com.example.financetrackerapplication.domain.repository.AuthRepository

class SignInWithGoogleUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(idToken: String): Result<Unit> =
        repo.signInWithGoogle(idToken)
}