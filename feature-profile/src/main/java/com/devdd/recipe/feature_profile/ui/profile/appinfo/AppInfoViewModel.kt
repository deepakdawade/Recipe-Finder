package com.devdd.recipe.feature_profile.ui.profile.appinfo

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.devdd.recipe.base.utils.AppBuildConfig
import com.devdd.recipe.data.preference.manager.GuestManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


/**
 * @author Deepak Dawade
 */
@HiltViewModel
class AppInfoViewModel @Inject constructor(
    private val appBuildConfig: AppBuildConfig,
    private val guestManager: GuestManager
):ViewModel() {


    val guestToken: LiveData<String> = guestManager.guestToken.asLiveData(viewModelScope.coroutineContext)

    val deviceId: LiveData<String> = guestManager.deviceId.asLiveData(viewModelScope.coroutineContext)

    val appVersion: String =
        "${appBuildConfig.VERSION_NAME} (${appBuildConfig.VERSION_CODE})"

    val deviceModel: String = "${Build.MODEL} (${Build.MANUFACTURER})"

    val osVersion: String = "${Build.VERSION.SDK_INT}"
}