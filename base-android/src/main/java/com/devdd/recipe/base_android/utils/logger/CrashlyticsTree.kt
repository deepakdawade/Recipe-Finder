package com.devdd.recipe.base_android.utils.logger

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashlyticsTree(private val crashlytics: FirebaseCrashlytics) : Timber.Tree() {
    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return priority >= Log.WARN
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        crashlytics.setCustomKey(CRASHLYTICS_KEY_PRIORITY, priority)
        tag?.let { crashlytics.setCustomKey(CRASHLYTICS_KEY_TAG, it) }
        crashlytics.setCustomKey(CRASHLYTICS_KEY_MESSAGE, message)
        crashlytics.log(message)
        crashlytics.recordException(t ?: return)
    }

    companion object {
        private const val CRASHLYTICS_KEY_PRIORITY = "priority"
        private const val CRASHLYTICS_KEY_TAG = "tag"
        private const val CRASHLYTICS_KEY_MESSAGE = "message"
    }
}