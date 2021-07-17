package com.devdd.recipe.ui.utils.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.launchDefaultBrowser(url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW)
    browserIntent.data = Uri.parse(url)
    browserIntent.resolveActivity(packageManager)?.let {
        startActivity(browserIntent)
    }
}