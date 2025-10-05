package com.example.financetrackerapplication.data.repository

import android.util.Log
import com.example.financetrackerapplication.data.datasource.local.datastore.SettingsPreferences
import com.example.financetrackerapplication.domain.model.UserStatus
import com.example.financetrackerapplication.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.actionCodeSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val settingsPreferences: SettingsPreferences
) : AuthRepository {

    override suspend fun signInAnonymously(): Result<Unit> = try {
        Log.d("AuthRepository", "Mulai login anonymous...")
        val user = firebaseAuth.currentUser
        if (user == null){
            firebaseAuth.signInAnonymously().await()
        }
        Log.d("AuthRepository", "Berhasil login anonymous")
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("AuthRepository", "Gagal login anonymous: ${e.message}")
        Result.failure(e)
    }

//    override suspend fun isUserLoggedIn(): Boolean {
//        val loggedIn = firebaseAuth.currentUser != null
//        Log.d("AuthRepository", "Cek user login: $loggedIn")
//        return loggedIn
//    }
//
//    override suspend fun isAnonymousUser(): Boolean {
//        val anonymous = firebaseAuth.currentUser?.isAnonymous == true
//        Log.d("AuthRepository", "Cek user anonymous: $anonymous")
//        return anonymous
//    }

    override suspend fun signInWithGoogle(idToken: String): Result<Unit> = try {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).await()
        Log.d("AuthRepository", "signInWithGoogle: credential = $credential")
        Result.success(Unit)
    }catch (e: Exception){
        Log.e("AuthRepository", "signInWithGoogle: Error", e)
        Result.failure(e)
    }

    override suspend fun signInWithLinkToEmail(email: String): Result<Unit> = try{
        val actionCodeSettings = actionCodeSettings {
            // URL must be whitelisted in the Firebase Console.
            url = "https://financetrackerapplication132.web.app"
            // This must be true
            handleCodeInApp = true
            iosBundleId = "com.example.ios"
            setAndroidPackageName(
                "com.example.financetrackerapplication",
                true, // installIfNotAvailable
                "12", // minimumVersion
            )
        }

        firebaseAuth.sendSignInLinkToEmail(email, actionCodeSettings)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Log.d("AuthRepository", "Email link sent to $email")
//                } else {
//                    Log.e("AuthRepository", "Error: ${task.exception?.message}")
//                }
//            }
        Log.d("AuthRepository", "signInWithLinkToEmail: email $email")
        Result.success(Unit)
    }catch (e: Exception){
        Log.e("AuthRepository", "signInWithLinkEmail: Error", e)
        Result.failure(e)
    }

    /**
     * 🔹 Kenapa nggak cukup salah satu aja?
     * Kalau cuma pakai getAuthState() → kamu harus manggil ulang manual setiap kali mau cek status. Ribet dan nggak reaktif.
     * Kalau cuma pakai observeAuthState() → kamu mungkin kesulitan saat butuh cek cepat sekali aja (misalnya di Splash).
     */
    override suspend fun getUserStatus(): UserStatus {
        val lastStatus = settingsPreferences.getAuthState()
        val user = firebaseAuth.currentUser
        Log.d("AuthRepository", "getAuthState: user = $user")
        Log.d("AuthRepository", "getAuthState: last status = $lastStatus")

        val state = when{
            user != null -> {
                if (user.isAnonymous) UserStatus.Anonymous else UserStatus.LoggedIn
            }
            else -> {
                if (lastStatus == null){
                    // pertama kali install
                    UserStatus.Anonymous
                }else{
                    // sudah pernah login/logout sebelumnya
                    UserStatus.LoggedOut
                }
            }
        }
        Log.d("AuthRepository", "getAuthState: user status is $state")
        settingsPreferences.saveAuthState(state.value) // simpan lo local pref
        return state
    }

    // pantau user status
    override fun observeAuthState(): Flow<UserStatus> =
        settingsPreferences.authStateFlow.map { UserStatus.fromString(it) }


    override fun signOut() {
        Log.d("AuthRepository", "User logout")
        firebaseAuth.signOut()
    }
}
