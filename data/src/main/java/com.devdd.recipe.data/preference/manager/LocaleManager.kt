package com.devdd.recipe.data.preference.manager

import android.content.Context
import com.devdd.recipe.base.constants.SelectedLocale.LOCALE_ENGLISH
import com.devdd.recipe.base.constants.SelectedLocale.LOCALE_HINDI
import com.devdd.recipe.base_android.utils.localemanager.LocaleManagerUtils
import com.devdd.recipe.data.preference.DataStorePreference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

class LocaleManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStore: DataStorePreference
) {
    val selectedLanguage: Flow<String>
        get() = dataStore.selectedLanguage.catch { emit("") }

    suspend fun isEnglishLocale(): Boolean = selectedLanguage.first() == LOCALE_ENGLISH

    suspend fun isHindiLocale(): Boolean = selectedLanguage.first() == LOCALE_HINDI

    suspend fun isLanguageSelected(): Boolean = selectedLanguage.first().isNotBlank()

    suspend fun isLanguageChanged(): Boolean =
        Locale.getDefault().language != selectedLanguage.first()

    suspend fun updateLanguage(language: String) {
        LocaleManagerUtils.setNewLocale(context = context, language = language)
        dataStore.setSelectedLocale(language)
    }
}