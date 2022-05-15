package com.devdd.recipe.ui

data class MainViewState(
    val recipePref: String = "",
    val localePref: String = "",
    val recipes: List<RecipeViewState> = emptyList()
) {
    val showOnBoarding: Boolean = recipePref.isBlank() || localePref.isBlank()

    companion object {
        val INITIAL = MainViewState()
    }
}
