package com.devdd.recipe.utils.localemanager

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build.VERSION_CODES.N
import android.os.LocaleList
import com.devdd.recipe.constants.SHARED_PREF_NAME
import com.devdd.recipe.data.prefs.manager.LocaleManager.Companion.LOCALE_ENGLISH
import com.devdd.recipe.data.prefs.manager.LocaleManager.Companion.LOCALE_HINDI
import com.devdd.recipe.utils.extensions.isAtLeastVersion
import java.util.*

object LocaleManagerUtils {

    private const val SELECTED_LOCALE = "selected_locale"
    private fun getPrefs(context: Context?): SharedPreferences? {
        return context?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }


    fun getLocale(res: Resources): Locale {
        val config = res.configuration
        return if (isAtLeastVersion(N)) config.locales.get(0) else config.locale
    }


    @SuppressLint("ApplySharedPref")
    private fun persistLanguage(context: Context, language: String) {
        // use commit() instead of apply(), because sometimes we kill the application process immediately
        // which will prevent apply() to finish
        getPrefs(context)?.edit()?.putString(SELECTED_LOCALE, language)?.commit()
    }

    fun setNewLocale(context: Context, language: String): Context {
        persistLanguage(context, language)
        return updateBaseContext(context, language)
    }


    @JvmStatic
    fun isEnglishLocale(context: Context?): Boolean =
        getLanguage(context) == LOCALE_ENGLISH

    @JvmStatic
    fun setLocale(c: Context): Context = updateBaseContext(c, getLanguage(c))

    fun getLanguage(context: Context?): String =
        getPrefs(context)?.getString(SELECTED_LOCALE, LOCALE_HINDI) ?: LOCALE_ENGLISH

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
