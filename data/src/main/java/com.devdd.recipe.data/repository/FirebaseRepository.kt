package com.devdd.recipe.data.repository

import android.content.Context
import com.devdd.recipe.base.utils.AppBuildConfig
import com.devdd.recipe.base.utils.AppCoroutineDispatchers
import com.devdd.recipe.data.preference.manager.GuestManager
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.Exception

interface FirebaseRepository {

    suspend fun fetchFcmToken()

    suspend fun sendTokenToServer(token: String)
}

class FirebaseRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val guestManager: GuestManager,
    private val firebaseMessaging: FirebaseMessaging,
    private val dispatchers: AppCoroutineDispatchers,
    private val appBuildConfig: AppBuildConfig
) : FirebaseRepository {
    override suspend fun fetchFcmToken() {
        firebaseMessaging.token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let { token ->
                    GlobalScope.launch(dispatchers.io) {
                        try {
                            sendTokenToServer(token)
                        } catch (e: Exception) {
                            Timber.e(e)
                        }
                    }
                }
            } else {
                Timber.e(task.exception)
            }
        }
    }

    override suspend fun sendTokenToServer(token: String) {
        guestManager.updateFcmToken(token)
    }
}