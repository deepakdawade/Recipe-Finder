/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.devdd.recipe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.devdd.recipe.R

private val YellowThemeLight = lightColors(
    primary = yellow500,
    primaryVariant = yellow400,
    onPrimary = Color.Black,
    secondary = blue700,
    secondaryVariant = blue800,
    onSecondary = Color.White
)

private val YellowThemeDark = darkColors(
    primary = yellow200,
    secondary = blue200,
    onSecondary = Color.Black,
    surface = yellowDarkPrimary
)

@Composable
fun YellowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        YellowThemeDark
    } else {
        YellowThemeLight
    }
    RecipeTheme(darkTheme, colors, content)
}

private val BlueThemeLight = lightColors(
    primary = blue700,
    onPrimary = Color.White,
    primaryVariant = blue800,
    secondary = yellow500
)

private val BlueThemeDark = darkColors(
    primary = blue200,
    secondary = yellow200,
    surface = blueDarkPrimary
)

@Composable
fun BlueTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        BlueThemeDark
    } else {
        BlueThemeLight
    }
    RecipeTheme(darkTheme, colors, content)
}

private val PinkThemeLight = lightColors(
    primary = pink500,
    secondary = pink500,
    primaryVariant = pink600,
    onPrimary = Color.Black,
    onSecondary = Color.Black
)

private val PinkThemeDark = darkColors(
    primary = pink200,
    secondary = pink200,
    surface = pinkDarkPrimary
)

@Composable
fun PinkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        PinkThemeDark
    } else {
        PinkThemeLight
    }
    RecipeTheme(darkTheme, colors, content)
}

private val LightElevation = Elevations()

private val DarkElevation = Elevations(card = 1.dp)

private val LightImages = Images(lockupLogo = R.drawable.ic_launcher_foreground)

private val DarkImages = Images(lockupLogo = R.drawable.ic_launcher_foreground)

@Composable
private fun RecipeTheme(
    darkTheme: Boolean,
    colors: Colors,
    content: @Composable () -> Unit
) {
    val elevation = if (darkTheme) DarkElevation else LightElevation
    val images = if (darkTheme) DarkImages else LightImages
    CompositionLocalProvider(
        LocalElevations provides elevation,
        LocalImages provides images
    ) {
        MaterialTheme(
            colors = colors,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}

/**
 * Alternate to [MaterialTheme] allowing us to add our own theme systems (e.g. [Elevations]) or to
 * extend [MaterialTheme]'s types e.g. return our own [Colors] extension
 */
object RecipeTheme {

    /**
     * Proxy to [MaterialTheme]
     */
    val colors: Colors
        @Composable
        get() = MaterialTheme.colors

    /**
     * Proxy to [MaterialTheme]
     */
    val typography: Typography
        @Composable
        get() = MaterialTheme.typography

    /**
     * Proxy to [MaterialTheme]
     */
    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes

    /**
     * Retrieves the current [Elevations] at the call site's position in the hierarchy.
     */
    val elevations: Elevations
        @Composable
        get() = LocalElevations.current

    /**
     * Retrieves the current [Images] at the call site's position in the hierarchy.
     */
    val images: Images
        @Composable
        get() = LocalImages.current
}
