package com.devdd.recipe.data.preference.manager

import com.devdd.recipe.data.models.response.UserInfo
import com.devdd.recipe.data.preference.DataStorePreference
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import javax.inject.Inject


/**
 * Created by @author Deepak Dawade on 4/13/2021 at 11:07 PM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
class GuestManager @Inject constructor(
    private val storePreference: DataStorePreference
) {
    val guestToken: Flow<String>
        get() = storePreference.guestToken.catch { emit("") }

    val fcmToken: Flow<String>
        get() = storePreference.fcmToken.catch { emit("") }

    val deviceId: Flow<String>
        get() = storePreference.deviceId.catch { emit("") }

    val shouldUploadDeviceIdToServer: Flow<Boolean>
        get() = storePreference.shouldUploadDeviceIdToServer

    val userLoggedIn: Flow<Boolean>
        get() = storePreference.userLoggedIn

    suspend fun setUserLogin(login: Boolean) {
        storePreference.setUserLoggedIn(login)
    }

    suspend fun setUserInfo(info: UserInfo?) {
        storePreference.setUserInfo(info)
    }

    val userInfo: Flow<UserInfo?>
        get() = storePreference.userInfo

    suspend fun updateGuestToken(token: String) {
        storePreference.setGuestToken(token)
    }

    suspend fun updateFcmToken(token: String) {
        storePreference.setFcmToken(token)
    }

    suspend fun guestToken(): String = guestToken.first()

    suspend fun fcmToken(): String = fcmToken.first()

    suspend fun deviceId(): String = deviceId.first()

    suspend fun guestTokenGenerated(): Boolean {
        val token = guestToken.first()
        return token.isNotEmpty()
    }

    suspend fun deviceIdGenerated(): Boolean {
        val token = deviceId.first()
        return token.isNotEmpty()
    }

    suspend fun shouldUploadDeviceIdToServer(shouldUpload: Boolean) {
        storePreference.shouldUploadDeviceIdToServer(shouldUpload)
    }

    fun generateDeviceId() {
        storePreference.generateDeviceId()
    }
}