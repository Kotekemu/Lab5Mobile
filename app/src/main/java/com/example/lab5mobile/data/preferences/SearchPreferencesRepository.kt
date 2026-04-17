package com.example.lab5mobile.data.preferences

import android.content.Context
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private const val SEARCH_PREFERENCES_NAME = "search_preferences"

private val Context.dataStore by preferencesDataStore(
    name = SEARCH_PREFERENCES_NAME
)

class SearchPreferencesRepository(
    private val context: Context
) {
    private companion object {
        val LAST_SEARCH_QUERY = stringPreferencesKey("last_search_query")
    }
    val lastSearchQuery: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences: Preferences ->
            preferences[LAST_SEARCH_QUERY] ?: ""
        }

    suspend fun saveLastSearchQuery(query: String) {
        context.dataStore.edit { preferences ->
            if (query.isBlank()) {
                preferences.remove(LAST_SEARCH_QUERY)
            } else {
                preferences[LAST_SEARCH_QUERY] = query
            }
        }
    }
}