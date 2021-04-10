package com.devdd.recipe.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.devdd.recipe.R
import com.devdd.recipe.databinding.ActivityMainBinding
import com.devdd.recipe.utils.extensions.bindingWithLifecycleOwner
import com.devdd.recipe.utils.localemanager.LocaleManagerUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingWithLifecycleOwner<ActivityMainBinding>(R.layout.activity_main)
    }

    override fun attachBaseContext(base: Context) {
        lifecycleScope.launch {
            super.attachBaseContext(LocaleManagerUtils.setLocale(base))
        }
    }

}