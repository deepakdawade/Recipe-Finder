package com.devdd.recipe.domain.mappers

import com.devdd.recipe.data.db.entities.Category
import com.devdd.recipe.data.mappers.Mapper
import com.devdd.recipe.data.remote.models.response.CategoryListResponse.CategoryResponse
import javax.inject.Inject

class CategoryResponseToCategoryEntity @Inject constructor(
) : Mapper<CategoryResponse, Category> {
    override suspend fun map(from: CategoryResponse): Category {
        return from.toCategoryEntity()
    }

    private fun CategoryResponse.toCategoryEntity(): Category {
        return Category(
            name = name ?: "",
            description = description ?: "",
            id = id ?: 0,
            imageUrl = imageUrl ?: ""
        )
    }
}