package com.getsung.tv.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(
    name = "app_user_preferences"
)

object PrefKeys {
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val SESSION_ID = stringPreferencesKey("session_id")
}

class DataStorePreferences @Inject constructor(
    @ApplicationContext context: Context,
    val securityUtil: SecurityUtil,
    val json: Json,
    private val scope: CoroutineScope
) {

    val bytesToStringSeperator = "|"
    val keyAlias = "appkey"
    val dataStore = context.dataStore
    val ivToStringSeparator = ":iv:"

    fun <T> getPreference(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[key] ?: defaultValue
            }
    }

    suspend fun <T> putPreference(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    fun <T> putPreferenceAsync(
        key: Preferences.Key<T>,
        value: T,
    ) {
        scope.launch {
            dataStore.edit { preferences ->
                preferences[key] = value
            }
        }
    }

    suspend fun <T> putSecurePreference(
        key: Preferences.Key<String>,
        value: T,
        serializer: KSerializer<T>
    ) {
        dataStore.edit { preferences ->
            val serializedInput = json.encodeToString(serializer, value)
            val (iv, secureByteArray) = securityUtil.encryptData(keyAlias, serializedInput)
            val secureString =
                iv.joinToString(bytesToStringSeperator) + ivToStringSeparator + secureByteArray.joinToString(
                    bytesToStringSeperator
                )
            preferences[key] = secureString
        }
    }

    suspend inline fun <reified T> getSecurePreference(
        key: Preferences.Key<String>,
        defaultValue: T
    ):
            Flow<T> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val secureString = preferences[key] ?: return@map defaultValue
        val (ivString, encryptedString) = secureString.split(ivToStringSeparator, limit = 2)
        val iv = ivString.split(bytesToStringSeperator).map { it.toByte() }.toByteArray()
        val encryptedData =
            encryptedString.split(bytesToStringSeperator).map { it.toByte() }.toByteArray()
        val decryptedValue = securityUtil.decryptData(keyAlias, iv, encryptedData)
        return@map try {
            json.decodeFromString<T>(decryptedValue)
        } catch (e: Exception) {
            defaultValue
        }
    }

    suspend fun <T> getSecureListPreference(
        key: Preferences.Key<String>,
        defaultValue: List<T>,
        serializer: KSerializer<T>
    ): Flow<List<T>> =
        dataStore.data.map { preferences ->

            val secureString = preferences[key] ?: return@map defaultValue

            val parts = secureString.split(ivToStringSeparator, limit = 2)
            if (parts.size != 2) return@map defaultValue

            val iv = parts[0]
                .split(bytesToStringSeperator)
                .mapNotNull { it.toByteOrNull() }
                .toByteArray()

            val encryptedData = parts[1]
                .split(bytesToStringSeperator)
                .mapNotNull { it.toByteOrNull() }
                .toByteArray()

            val decryptedValue = runCatching {
                securityUtil.decryptData(keyAlias, iv, encryptedData)
            }.getOrNull() ?: return@map defaultValue

            runCatching {
                json.decodeFromString(
                    ListSerializer(serializer),
                    decryptedValue
                )
            }.getOrElse {
                defaultValue
            }
        }


    suspend fun <T> removePreference(key: Preferences.Key<T>) {
        dataStore.edit {
            it.remove(key)
        }
    }

    suspend fun clearAllPreference() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}