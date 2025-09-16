package com.example.financetrackerapplication.data.repository

import android.util.Log
import com.example.financetrackerapplication.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun signInAnonymously(): Result<Unit> = try {
        Lzog.d("AuthRepository", "Mulai login anonymous...")
        firebaseAuth.signInAnonymously().await()
        Log.d("AuthRepository", "Berhasil login anonymous")
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("AuthRepository", "Gagal login anonymous: ${e.message}")
        Result.failure(e)
    }

    override suspend fun isUserLoggedIn(): Boolean {
        val loggedIn = firebaseAuth.currentUser != null
        Log.d("AuthRepository", "Cek user login: $loggedIn")
        return loggedIn
    }

    override suspend fun isAnonymousUser(): Boolean {
        val anonymous = firebaseAuth.currentUser?.isAnonymous == true
        Log.d("AuthRepository", "Cek user anonymous: $anonymous")
        return anonymous
    }

    override fun signOut() {
        Log.d("AuthRepository", "User logout")
        firebaseAuth.signOut()
    }
}
