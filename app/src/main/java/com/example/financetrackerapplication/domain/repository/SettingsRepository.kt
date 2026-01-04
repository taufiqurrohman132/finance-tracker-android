package com.example.financetrackerapplication.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val authStateFlow: Flow<String>

    val themeModeFlow: Flow<Int>
    val notificationEnableFlow: Flow<Boolean>

    suspend fun saveAuthState(state: String)
    suspend fun getAuthState(): String?

    suspend fun setThemeMode(mode: Int)
    suspend fun setNotificationEnabled(enabled: Boolean)
}