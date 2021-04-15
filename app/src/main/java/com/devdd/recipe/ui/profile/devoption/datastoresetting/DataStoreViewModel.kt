package com.devdd.recipe.ui.profile.devoption.datastoresetting

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.*
import com.devdd.recipe.data.prefs.DataStorePreference
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

    val selectedValue: MutableLiveData<Any> = MutableLiveData<Any>()
    private val selectedKey = MutableLiveData<Preferences.Key<*>>()

    fun setValueAtPosition(position: Int) {
        val set: Set<Preferences.Key<*>>? = prefKeys.value
        val key = set?.elementAt(position)
        key?.let { setValueAtKey(it) }
    }

    private fun setValueAtKey(key: Preferences.Key<*>) {
        val data = data.value
        val value: Any? = data?.get(key)
        selectedKey.value = key
        selectedValue.value = value ?: Any()
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
        val value = selectedValue.value ?: return
        viewModelScope.launch {
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