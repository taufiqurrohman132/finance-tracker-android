package com.example.financetrackerapplication.data.datasource.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.financetrackerapplication.domain.model.UserStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object SettingsPreferences {
    val AUTH_STATE = stringPreferencesKey("auth_state")
    val THEME_MODE = intPreferencesKey("theme_mode")
    val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")

}