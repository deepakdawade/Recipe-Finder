package com.devdd.recipe.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.devdd.recipe.R
import com.devdd.recipe.databinding.ActivityMainBinding
import com.devdd.recipe.utils.extensions.bindingWithLifecycleOwner
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingWithLifecycleOwner<ActivityMainBinding>(R.layout.activity_main)
    }
}