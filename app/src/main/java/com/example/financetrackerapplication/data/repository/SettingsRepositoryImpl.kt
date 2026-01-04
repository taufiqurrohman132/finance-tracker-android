package com.example.financetrackerapplication.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.financetrackerapplication.data.datasource.local.datastore.SettingsPreferences
import com.example.financetrackerapplication.domain.model.UserStatus
import com.example.financetrackerapplication.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {
    override val authStateFlow: Flow<String>
        get() = dataStore.data.map {
            it[SettingsPreferences.AUTH_STATE] ?: UserStatus.LoggedOut.value
        }
    override val themeModeFlow: Flow<Int>
        get() = dataStore.data.map { it[SettingsPreferences.THEME_MODE] ?: 0 }
    override val notificationEnableFlow: Flow<Boolean>
        get() = dataStore.data.map { it[SettingsPreferences.NOTIFICATION_ENABLED] ?: false}

    override suspend fun saveAuthState(state: String) {
        dataStore.edit { pref ->
            pref[SettingsPreferences.AUTH_STATE] = state
        }
    }

    override suspend fun getAuthState(): String? {
        return authStateFlow.firstOrNull() // ambil nilai terakhir/ jika kosong = null
    }

    override suspend fun setThemeMode(mode: Int) {
        dataStore.edit {
            it[SettingsPreferences.THEME_MODE] = mode
        }
    }

    override suspend fun setNotificationEnabled(enabled: Boolean) {
        dataStore.edit {
            it[SettingsPreferences.NOTIFICATION_ENABLED] = enabled
        }
    }
}