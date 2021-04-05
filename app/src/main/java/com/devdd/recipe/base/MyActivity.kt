package com.devdd.recipe.base

import androidx.appcompat.app.AppCompatActivity

abstract class MyActivity : AppCompatActivity() {

    @Suppress("PropertyName")
    val TAG: String = this::class.java.simpleName
}