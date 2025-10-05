package com.example.financetrackerapplication.domain.repository

import com.example.financetrackerapplication.domain.model.UserStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.descriptors.PrimitiveKind

interface AuthRepository {
    suspend fun signInAnonymously(): Result<Unit>
//    suspend fun isUserLoggedIn(): Boolean
//    suspend fun isAnonymousUser(): Boolean
    suspend fun signInWithGoogle(idToken: String): Result<Unit>
    suspend fun signInWithLinkToEmail(email: String): Result<Unit>
    suspend fun getUserStatus(): UserStatus
    fun observeAuthState(): Flow<UserStatus>
    fun signOut()
}