package com.devdd.recipe.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.devdd.recipe.utils.extensions.dataStore
import com.devdd.recipe.utils.extensions.getValueAsFlow
import com.devdd.recipe.utils.extensions.setValue
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface DataStorePreference {

    suspend fun setGuestToken(token: String)
    val guestToken: Flow<String>

    suspend fun setRecipePreference(pref: String)
    val recipePreference: Flow<String>

    suspend fun setSelectedLocale(language: String)
    val selectedLanguage: Flow<String>

    /***
     * clears all the stored data
     */
    suspend fun clearPreferences()
}

@Singleton
class DataStorePreferences @Inject constructor(@ApplicationContext private val context: Context) :
    DataStorePreference {

    private val dataStore: DataStore<Preferences>
        get() = context.dataStore

    /**
     * override preferences
     * */

    override suspend fun setGuestToken(token: String) {
        dataStore.setValue(PreferencesKeys.PREF_KEY_GUEST_TOKEN, token)
    }

    override val guestToken: Flow<String>
        get() = dataStore.getValueAsFlow(PreferencesKeys.PREF_KEY_GUEST_TOKEN,"")


    override suspend fun setRecipePreference(pref: String) {
        dataStore.setValue(PreferencesKeys.PREF_KEY_SELECTED_RECIPE_PREF, pref)
    }

    override val recipePreference: Flow<String>
        get() = dataStore.getValueAsFlow(PreferencesKeys.PREF_KEY_SELECTED_RECIPE_PREF, "")

    override suspend fun setSelectedLocale(language: String) {
        dataStore.setValue(PreferencesKeys.PREF_KEY_SELECTED_LANGUAGE, language)
    }

    override val selectedLanguage: Flow<String>
        get() = dataStore.getValueAsFlow(PreferencesKeys.PREF_KEY_SELECTED_LANGUAGE, "")

    override suspend fun clearPreferences() {
        dataStore.edit {
            it.clear()
        }
    }

    private object PreferencesKeys {

        object PreferencesName {
            const val GUEST_TOKEN = "guest_token"
            const val SELECTED_RECIPE = "selected_recipe_pref"
            const val SELECTED_LANGUAGE = "selected_language"
        }

        val PREF_KEY_GUEST_TOKEN = stringPreferencesKey(PreferencesName.GUEST_TOKEN)
        val PREF_KEY_SELECTED_RECIPE_PREF = stringPreferencesKey(PreferencesName.SELECTED_RECIPE)
        val PREF_KEY_SELECTED_LANGUAGE = stringPreferencesKey(PreferencesName.SELECTED_LANGUAGE)
    }


}