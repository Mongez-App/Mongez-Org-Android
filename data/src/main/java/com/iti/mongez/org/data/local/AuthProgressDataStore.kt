package com.iti.mongez.org.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_progress")

@Singleton
class AuthProgressDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
        private val KEY_REG_STEP = intPreferencesKey("registration_step")
        private val KEY_DRAFT_ORG = stringPreferencesKey("draft_org_json")
    }

    suspend fun saveToken(token: String) {
        context.authDataStore.edit { prefs ->
            prefs[KEY_AUTH_TOKEN] = token
        }
    }

    suspend fun getToken(): String? {
        return context.authDataStore.data.map { prefs ->
            prefs[KEY_AUTH_TOKEN]
        }.first()
    }

    suspend fun saveStep(step: Int) {
        context.authDataStore.edit { prefs ->
            prefs[KEY_REG_STEP] = step
        }
    }

    suspend fun getStep(): Int {
        return context.authDataStore.data.map { prefs ->
            prefs[KEY_REG_STEP] ?: 0
        }.first()
    }

    suspend fun saveDraft(draftJson: String) {
        context.authDataStore.edit { prefs ->
            prefs[KEY_DRAFT_ORG] = draftJson
        }
    }

    suspend fun getDraft(): String? {
        return context.authDataStore.data.map { prefs ->
            prefs[KEY_DRAFT_ORG]
        }.first()
    }

    suspend fun clear() {
        context.authDataStore.edit { it.clear() }
    }
}
