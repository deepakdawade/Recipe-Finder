package com.devdd.recipe.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.devdd.recipe.constants.DATA_STORE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

interface RecipeDataStore {

    val settingSaved: Flow<Boolean>
    suspend fun setSettingSaved(saved: Boolean)

    /***
     * clears all the stored data
     */
    suspend fun clearPreferences()
}

@Singleton
class RecipeDataStoreImpl @Inject constructor(@ApplicationContext private val context: Context) :
    RecipeDataStore {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)
    private val dataStore: DataStore<Preferences>
        get() = context.dataStore

    override val settingSaved: Flow<Boolean>
        get() = dataStore.getValueAsFlow(PreferencesKeys.SETTING_SAVED_KEY, false)

    override suspend fun setSettingSaved(saved: Boolean) {
        dataStore.setValue(PreferencesKeys.SETTING_SAVED_KEY, saved)
    }

    override suspend fun clearPreferences() {
        dataStore.edit {
            it.clear()
        }
    }

    private object PreferencesKeys {
        val SETTING_SAVED_KEY = booleanPreferencesKey("setting_saved")
    }

    /***
     * handy function to save key-value pairs in Preference. Sets or updates the value in Preference
     * @param key used to identify the preference
     * @param value the value to be saved in the preference
     */
    private suspend fun <T> DataStore<Preferences>.setValue(
        key: Preferences.Key<T>,
        value: T
    ) {
        this.edit { preferences ->
            // save the value in prefs
            preferences[key] = value
        }
    }

    /***
     * handy function to return Preference value based on the Preference key
     * @param key  used to identify the preference
     * @param defaultValue value in case the Preference does not exists
     * @throws Exception if there is some error in getting the value
     * @return [Flow] of [T]
     */
    private fun <T> DataStore<Preferences>.getValueAsFlow(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> {
        return this.data.catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                // we try again to store the value in the map operator
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            // return the default value if it doesn't exist in the storage
            preferences[key] ?: defaultValue
        }
    }
}
