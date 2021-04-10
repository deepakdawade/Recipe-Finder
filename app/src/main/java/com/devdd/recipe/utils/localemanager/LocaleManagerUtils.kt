package com.devdd.recipe.utils.localemanager

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build.VERSION_CODES.N
import android.os.LocaleList
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.devdd.recipe.data.prefs.manager.LocaleManager.Companion.LOCALE_ENGLISH
import com.devdd.recipe.data.prefs.manager.LocaleManager.Companion.LOCALE_HINDI
import com.devdd.recipe.utils.extensions.dataStore
import com.devdd.recipe.utils.extensions.getValueAsFlow
import com.devdd.recipe.utils.extensions.isAtLeastVersion
import kotlinx.coroutines.flow.first
import java.util.*


object LocaleManagerUtils {
    private val SELECTED_LOCALE = stringPreferencesKey("language_preference")
    private fun dataStore(context: Context): DataStore<Preferences> = context.dataStore

    @JvmStatic
    suspend fun isEnglishLocale(context: Context): Boolean =
        getLanguage(context) == LOCALE_ENGLISH

    @JvmStatic
    suspend fun isHindiLocale(context: Context): Boolean =
        getLanguage(context) == LOCALE_HINDI

    @JvmStatic
    suspend fun setLocale(context: Context): Context =
        updateBaseContext(context, getLanguage(context))

    suspend fun getLanguage(context: Context): String =
        dataStore(context).getValueAsFlow(SELECTED_LOCALE, LOCALE_HINDI).first()

    @JvmStatic
    private fun updateBaseContext(context: Context, language: String): Context {
        val locale = if (language == LOCALE_HINDI) Locale(language, "IN")
        else Locale(language, "IN")

        Locale.setDefault(locale)

        val res: Resources = context.resources
        val configuration = Configuration(res.configuration)

        return when {
            isAtLeastVersion(N) -> {
                val set: MutableSet<Locale> = LinkedHashSet()
                // bring the target locale to the front of the list
                set.add(locale)
                val all = LocaleList.getDefault()
                for (i in 0 until all.size()) {
                    // append other locales supported by the user
                    set.add(all[i])
                }
                val locales = set.toTypedArray()
                configuration.setLocales(LocaleList(*locales))
                configuration.setLocale(locale)
                context.createConfigurationContext(configuration)
            }
            else -> {
                configuration.fontScale = 1f
                configuration.setLocale(locale)
                context.createConfigurationContext(configuration)
            }
        }
    }
}