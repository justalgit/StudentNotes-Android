package com.example.studentnotes.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

suspend fun DataStore<Preferences>.save(key: String, value: String) {
    val dataStoreKey = stringPreferencesKey(key)
    edit { settings ->
        settings[dataStoreKey] = value
    }
}

suspend fun DataStore<Preferences>.read(key: String): String? {
    val dataStoreKey = stringPreferencesKey(key)
    return try {
        val preferences = data.first()
        preferences[dataStoreKey]
    } catch(e: NoSuchElementException) {
        null
    }
}