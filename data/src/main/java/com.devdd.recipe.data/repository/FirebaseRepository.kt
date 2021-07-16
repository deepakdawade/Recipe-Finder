package com.devdd.recipe.data.repository

import com.devdd.recipe.base.result.InvokeError
import com.devdd.recipe.base.result.InvokeStarted
import com.devdd.recipe.base.result.InvokeStatus
import com.devdd.recipe.base.result.InvokeSuccess
import com.devdd.recipe.base.utils.AppCoroutineDispatchers
import com.devdd.recipe.base.utils.Logger
import com.devdd.recipe.data.models.response.toUserInfo
import com.devdd.recipe.data.preference.manager.GuestManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

interface FirebaseRepository {

    val loginStatus: Flow<InvokeStatus<Boolean>>

    suspend fun loggedIn(token: String)

    suspend fun login(emailId: String, password: String)

    suspend fun registerUser(emailId: String, password: String)

    suspend fun logout()

    suspend fun fetchFcmToken()

    suspend fun sendTokenToServer(token: String)
}

class FirebaseRepositoryImpl @Inject constructor(
    private val logger: Logger,
    private val guestManager: GuestManager,
    private val auth: FirebaseAuth,
    private val firebaseMessaging: FirebaseMessaging,
    private val dispatchers: AppCoroutineDispatchers
) : FirebaseRepository {
    private val mLoginStatus: MutableStateFlow<InvokeStatus<Boolean>> =
        MutableStateFlow(InvokeStarted)
    override val loginStatus: Flow<InvokeStatus<Boolean>> get() = mLoginStatus

    override suspend fun loggedIn(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        mLoginStatus.value = InvokeStarted
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            try {
                if (task.isSuccessful) {
                    setUserLoggedIn()
                    mLoginStatus.value = InvokeSuccess(true)
                } else
                    mLoginStatus.value = InvokeError(task.exception ?: Exception("Error Occurred"))
            } catch (e: ClosedSendChannelException) {
                logger.e(e)
                mLoginStatus.value = InvokeError(e)
            }
        }
    }

    override suspend fun login(emailId: String, password: String) {
        mLoginStatus.value = InvokeStarted
        auth.signInWithEmailAndPassword(emailId, password).addOnCompleteListener { task ->
            try {
                if (task.isSuccessful) {
                    setUserLoggedIn()
                    mLoginStatus.value = InvokeSuccess(true)
                } else
                    mLoginStatus.value = InvokeError(task.exception ?: Exception("Error Occurred"))
            } catch (e: ClosedSendChannelException) {
                logger.e(e)
                mLoginStatus.value = InvokeError(e)
            }
        }
    }

    override suspend fun registerUser(emailId: String, password: String) {
        mLoginStatus.value = InvokeStarted
        auth.createUserWithEmailAndPassword(emailId, password).addOnCompleteListener { task ->
            try {
                if (task.isSuccessful) {
                    setUserLoggedIn()
                    mLoginStatus.value = InvokeSuccess(true)
                } else
                    mLoginStatus.value = InvokeError(task.exception ?: Exception("Error Occurred"))
            } catch (e: ClosedSendChannelException) {
                logger.e(e)
                mLoginStatus.value = InvokeError(e)
            }
        }
    }

    private fun setUserLoggedIn() {
        GlobalScope.launch {
            guestManager.setUserLogin(true)
            guestManager.setUserInfo(auth.currentUser?.toUserInfo())
        }
    }

    override suspend fun logout() {
        guestManager.setUserLogin(false)
        auth.signOut()
        guestManager.setUserInfo(auth.currentUser?.toUserInfo())
    }

    override suspend fun fetchFcmToken() {
        firebaseMessaging.token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let { token ->
                    GlobalScope.launch(dispatchers.io) {
                        try {
                            sendTokenToServer(token)
                            Timber.e("Fcm Token: $token")
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