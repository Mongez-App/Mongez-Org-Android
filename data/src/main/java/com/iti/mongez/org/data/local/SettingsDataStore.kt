package com.iti.mongez.org.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val KEY_IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        private val KEY_LANGUAGE = stringPreferencesKey("language")
    }

    val isDarkMode: Flow<Boolean?> = context.settingsDataStore.data.map { prefs ->
        prefs[KEY_IS_DARK_MODE]
    }

    val language: Flow<String> = context.settingsDataStore.data.map { prefs ->
        prefs[KEY_LANGUAGE] ?: "en"
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[KEY_IS_DARK_MODE] = enabled
        }
    }

    suspend fun setLanguage(language: String) {
        context.settingsDataStore.edit { prefs ->
            prefs[KEY_LANGUAGE] = language.lowercase()
        }
    }
}
