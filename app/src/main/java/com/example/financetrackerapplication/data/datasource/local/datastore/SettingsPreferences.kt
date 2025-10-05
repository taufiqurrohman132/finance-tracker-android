package com.example.financetrackerapplication.data.datasource.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.financetrackerapplication.domain.model.UserStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    // auth state
    val authStateFlow: Flow<String> = dataStore.data.map {
        it[AUTH_STATE] ?: UserStatus.LoggedOut.value
    }

    suspend fun saveAuthState(state: String){
        dataStore.edit { pref ->
            pref[AUTH_STATE] = state
        }
    }

    suspend fun getAuthState(): String? =
        authStateFlow.firstOrNull() // ambil nilai terakhir/ jika kosong = null

    companion object {
        private val AUTH_STATE = stringPreferencesKey("auth_state")
    }
}