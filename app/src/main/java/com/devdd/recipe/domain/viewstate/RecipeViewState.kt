package com.devdd.recipe.domain.viewstate

data class RecipeViewState(
    val id: Int = 0,
    val authorName: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val ingredients: List<String> = emptyList(),
    val totalTime: String = "",
    val cookingTime: String = "",
    val preparingTime: String = "",
    val categoryName: String = "",
) {
    fun getIngredients(): String {
        val string = StringBuilder()
        ingredients.forEach {
            string.append(it).append("\n")
        }
        return string.toString()
    }
}