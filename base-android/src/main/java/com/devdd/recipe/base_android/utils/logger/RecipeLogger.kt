package com.devdd.recipe.base_android.utils.logger

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.devdd.recipe.base.utils.Logger
import com.devdd.recipe.base.utils.extensions.debugElseRelease
import com.devdd.recipe.base_android.utils.extensions.buildType
import timber.log.Timber

class RecipeLogger(private val firebaseCrashlytics: FirebaseCrashlytics) : Logger {

    override fun init() {
        buildType.debugElseRelease(
            whenDebugging = { Timber.plant(TimberDebugTree()) },
            onProduction = { Timber.plant(CrashlyticsTree(firebaseCrashlytics)) }
        )
    }

    override fun setUserId(id: String) {
        firebaseCrashlytics.setUserId(id)
    }

    override fun v(message: String, vararg args: Any?) {
        Timber.v(message, *args)
    }

    override fun v(t: Throwable, message: String, vararg args: Any?) {
        Timber.v(t, message, *args)
    }

    override fun v(t: Throwable) {
        Timber.v(t)
    }

    override fun d(message: String, vararg args: Any?) {
        Timber.d(message, *args)
    }

    override fun d(t: Throwable, message: String, vararg args: Any?) {
        Timber.d(t, message, *args)
    }

    override fun d(t: Throwable) {
        Timber.d(t)
    }

    override fun i(message: String, vararg args: Any?) {
        Timber.i(message, *args)
    }

    override fun i(t: Throwable, message: String, vararg args: Any?) {
        Timber.i(t, message, *args)
    }

    override fun i(t: Throwable) {
        Timber.i(t)
    }

    override fun w(message: String, vararg args: Any?) {
        Timber.w(message, *args)
    }

    override fun w(t: Throwable, message: String, vararg args: Any?) {
        Timber.w(t, message, *args)
    }

    override fun w(t: Throwable) {
        Timber.w(t)
    }

    override fun e(message: String, vararg args: Any?) {
        Timber.e(message, *args)
    }

    override fun e(t: Throwable, message: String, vararg args: Any?) {
        Timber.e(t, message, *args)
    }

    override fun e(t: Throwable) {
        Timber.e(t)
    }

    override fun wtf(message: String, vararg args: Any?) {
        Timber.wtf(message, *args)
    }

    override fun wtf(t: Throwable, message: String, vararg args: Any?) {
        Timber.wtf(t, message, *args)
    }

    override fun wtf(t: Throwable) {
        Timber.wtf(t)
    }
}