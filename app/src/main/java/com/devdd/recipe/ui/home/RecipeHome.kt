package com.devdd.recipe.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.devdd.recipe.ui.RecipeViewState

@Composable
fun RecipeHome(
    recipes: List<RecipeViewState>,
    selectRecipes: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Home coming \nrecipes found: ${recipes.size}",
            style = MaterialTheme.typography.body2
        )
    }
}