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
    val savedDate: String = "",
    val saved: Boolean = false
) {
    fun getIngredients(): String {
        val builder = StringBuilder()
        ingredients.forEach {
            builder
                .append("\u25CF\t")
                .append(it)
                .append("\n")
        }
        return builder.toString()
    }
}