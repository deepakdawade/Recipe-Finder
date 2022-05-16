package com.devdd.recipe.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.devdd.recipe.utils.AppLocale
import com.devdd.recipe.utils.RecipePreference
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import  com.devdd.recipe.R
import com.google.accompanist.pager.PagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoarding(
    selectedIndex: Int,
    selectedLocale: String,
    selectedRecipePref: String,
    onBoardingComplete: () -> Unit,
    updateRecipePref: (pref: String) -> Unit,
    updateAppLocale: (locale: String) -> Unit
) {
    if (selectedLocale.isNotBlank() && selectedRecipePref.isNotBlank())
        onBoardingComplete()
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val updatePagerIndex: (index: Int) -> Unit = {
        coroutineScope.launch {
            pagerState.animateScrollToPage(it)
        }
    }
    LaunchedEffect(true) {
        if (selectedIndex > 0) {
            updatePagerIndex(selectedIndex)
        }
    }
    OnBoardingContent(
        pagerState,
        onBoardingComplete,
        updateAppLocale,
        updateRecipePref,
        updatePagerIndex
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingContent(
    pagerState: PagerState,
    onBoardingComplete: () -> Unit,
    updateAppLocale: (locale: String) -> Unit,
    updateRecipePref: (pref: String) -> Unit,
    updatePagerIndex: (index: Int) -> Unit
) {
    HorizontalPager(count = 2, userScrollEnabled = false, state = pagerState) {
        when (it) {
            0 -> OnBoardingLanguagePreference(
                updateAppLocale = { locale ->
                    updateAppLocale(locale)
                    updatePagerIndex(1)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
            1 -> OnBoardingRecipePreference(
                updateRecipePref = { recipePref ->
                    updateRecipePref(recipePref)
                    onBoardingComplete()
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun OnBoardingLanguagePreference(
    modifier: Modifier,
    updateAppLocale: (locale: String) -> Unit
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Choose Preferred Language", style = MaterialTheme.typography.h6
                )
                Text(
                    text = "पसंदीदा भाषा चुनें", style = MaterialTheme.typography.h6
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1F)
                        .aspectRatio(1F)
                        .background(MaterialTheme.colors.onSecondary, RoundedCornerShape(12.dp))
                        .border(BorderStroke(1.dp, MaterialTheme.colors.onSurface))
                        .clickable {
                            updateAppLocale(AppLocale.LOCALE_ENGLISH)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "English")
                }
                Box(
                    modifier = Modifier
                        .weight(1F)
                        .aspectRatio(1F)
                        .background(MaterialTheme.colors.onSecondary, RoundedCornerShape(12.dp))
                        .border(BorderStroke(1.dp, MaterialTheme.colors.onSurface))
                        .clickable {
                            updateAppLocale(AppLocale.LOCALE_HINDI)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "हिन्दी")
                }
            }
        }
    }
}

@Composable
private fun OnBoardingRecipePreference(
    modifier: Modifier,
    updateRecipePref: (pref: String) -> Unit
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.choose_recipe_preference),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        updateRecipePref(RecipePreference.VEG)
                    }
                ) {
                    Text(text = stringResource(id = R.string.veg))
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        updateRecipePref(RecipePreference.NON_VEG)
                    }
                ) {
                    Text(text = stringResource(id = R.string.nonveg))
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        updateRecipePref(RecipePreference.BOTH)
                    }
                ) {
                    Text(text = stringResource(id = R.string.both))
                }
            }
        }
    }

}
