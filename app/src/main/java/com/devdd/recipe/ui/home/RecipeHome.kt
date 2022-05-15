package com.devdd.recipe.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.devdd.recipe.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.devdd.recipe.ui.RecipeViewState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState

@Composable
fun RecipeHome(
    refreshState: SwipeRefreshState,
    recipes: List<RecipeViewState>,
    selectRecipes: (Long) -> Unit,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit
) {
    SwipeRefresh(state = refreshState, onRefresh = onRefresh, modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(recipes) {
                RecipeCard(
                    recipe = it,
                    onClick = selectRecipes,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RecipeCard(
    modifier: Modifier,
    recipe: RecipeViewState,
    onClick: (id: Long) -> Unit
) {
    Card(modifier = modifier, shape = RoundedCornerShape(12.dp), onClick = {
        onClick(recipe.entity.id.toLong())
    }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val placeHolder =
                rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground))
            AsyncImage(
                model = recipe.entity.imageUrl,
                contentDescription = "Recipe Image",
                modifier = Modifier
                    .fillMaxWidth(0.4F)
                    .aspectRatio(1F),
                error = placeHolder,
                placeholder = placeHolder,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.weight(1F),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = recipe.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = recipe.savedDate,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}