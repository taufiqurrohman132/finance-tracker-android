package com.example.financetrackerapplication.domain.repository

import kotlinx.serialization.descriptors.PrimitiveKind

interface AuthRepository {
    suspend fun signInAnonymously(): Result<Unit>
    suspend fun isUserLoggedIn(): Boolean
    suspend fun isAnonymousUser(): Boolean
    fun signOut()
}