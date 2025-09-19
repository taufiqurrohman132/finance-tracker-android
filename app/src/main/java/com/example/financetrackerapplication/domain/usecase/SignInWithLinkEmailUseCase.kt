package com.example.financetrackerapplication.domain.usecase

import com.example.financetrackerapplication.domain.repository.AuthRepository

class SignInWithLinkEmailUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(email: String): Result<Unit> =
        repo.signInWithLinkToEmail(email)

}