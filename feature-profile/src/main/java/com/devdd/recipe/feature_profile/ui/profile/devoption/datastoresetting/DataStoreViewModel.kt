package com.devdd.recipe.feature_profile.ui.profile.devoption.datastoresetting

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.*
import com.devdd.recipe.data.preference.DataStorePreference
import com.devdd.recipe.utils.extensions.premitive.isBoolean
import com.devdd.recipe.utils.extensions.premitive.isInt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(
    private val storePreference: DataStorePreference
) : ViewModel() {

    private val data: MutableLiveData<Map<Preferences.Key<*>, Any>> = MutableLiveData()
    private val prefKeys: LiveData<Set<Preferences.Key<*>>> = data.map {
        it.keys
    }

    val prefNames: LiveData<List<String>> = prefKeys.map {
        it.map { key ->
            key.name
        }
    }

    val selectedValue: MutableLiveData<String> = MutableLiveData<String>()
    private val selectedKey = MutableLiveData<Preferences.Key<*>>()

    fun getKeyAtPosition(position: Int): Preferences.Key<*>? {
        val set: Set<Preferences.Key<*>>? = prefKeys.value
        val key = set?.elementAt(position)
        key?.let { getValueAtKey(it) }
        return key
    }

    private fun getValueAtKey(key: Preferences.Key<*>): Any? {
        val data = data.value
        val value: Any? = data?.get(key)
        selectedKey.value = key
        selectedValue.value = value.toString()
        return value
    }

    init {
        initPreferences()
    }

    private fun initPreferences() {
        viewModelScope.launch {
            storePreference.preferences.collect {
                data.postValue(it.asMap())
            }
        }
    }

    fun putKey() {
        val key = selectedKey.value ?: return
        val any = selectedValue.value ?: return
        viewModelScope.launch {
            val value = when {
                any.isInt() -> any.toInt()
                any.isBoolean() -> any.toBoolean()
                any.isNotBlank() -> any
                else -> return@launch
            }
            storePreference.setValue(key, value)
        }
    }

    fun removeKey() {
        val key = selectedKey.value ?: return
        viewModelScope.launch {
            storePreference.remove(key)
        }
    }
}