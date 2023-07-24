package com.masrofy.repository.category_repository

import com.masrofy.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getCategories():List<Category>
    fun observeCategories():Flow<List<Category>>
    suspend fun insertCategory(category: Category)
    suspend fun insertCategory(category: List<Category>)
    suspend fun deleteCategory(category: Category)
}