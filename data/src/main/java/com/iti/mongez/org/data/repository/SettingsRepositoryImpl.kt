package com.iti.mongez.org.data.repository

import com.iti.mongez.org.data.local.SettingsDataStore
import com.iti.mongez.org.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : SettingsRepository {
    override val isDarkMode: Flow<Boolean?> = settingsDataStore.isDarkMode
    override val language: Flow<String> = settingsDataStore.language

    override suspend fun setDarkMode(enabled: Boolean) {
        settingsDataStore.setDarkMode(enabled)
    }

    override suspend fun setLanguage(language: String) {
        settingsDataStore.setLanguage(language)
    }
}
