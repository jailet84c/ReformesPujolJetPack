package com.jetPackReformesPujol.login.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "login data")

class UserPreferences(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val EMAIL_KEY = stringPreferencesKey("email")
        val PASSWORD_KEY = stringPreferencesKey("password")
    }

    suspend fun saveUserCredentials(email: String, password: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
            preferences[PASSWORD_KEY] = password
        }
    }

    val userEmail = dataStore.data.map { preferences ->
        preferences[EMAIL_KEY] ?: ""
    }

    val userPassword = dataStore.data.map { preferences ->
        preferences[PASSWORD_KEY] ?: ""
    }
}
