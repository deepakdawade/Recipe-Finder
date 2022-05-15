package com.devdd.recipe.data.local.pref.manager

import android.content.Context
import com.devdd.recipe.data.local.pref.DataStorePreference
import com.devdd.recipe.utils.AppLocale
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class LocaleManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStore: DataStorePreference
) {
    val selectedLanguage: Flow<String> = dataStore.selectedLanguage.catch { emit("") }

    fun isEnglishLocale(locale: String): Boolean = locale == AppLocale.LOCALE_ENGLISH

    fun isHindiLocale(locale: String): Boolean = locale == AppLocale.LOCALE_HINDI

    val isLanguageSelected: Flow<Boolean> = selectedLanguage.map { it.isNotBlank() }

    suspend fun updateLanguage(language: String) {
        dataStore.setSelectedLocale(language)
    }
}