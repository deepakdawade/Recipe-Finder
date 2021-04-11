package com.devdd.recipe.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.devdd.recipe.R
import com.devdd.recipe.databinding.ActivityMainBinding
import com.devdd.recipe.utils.extensions.bindingWithLifecycleOwner
import com.devdd.recipe.utils.localemanager.LocaleManagerUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingWithLifecycleOwner<ActivityMainBinding>(R.layout.activity_main)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleManagerUtils.setLocale(base))
    }
}