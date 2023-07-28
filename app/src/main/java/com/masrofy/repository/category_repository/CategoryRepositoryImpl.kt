package com.masrofy.repository.category_repository

import com.masrofy.data.dao.CategoryDao
import com.masrofy.mapper.toCategory
import com.masrofy.mapper.toCategoryEntity
import com.masrofy.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
):CategoryRepository {
    override suspend fun getCategories(): List<Category> {
        return categoryDao.getCategories().toCategory()
    }

    override fun observeCategories(): Flow<List<Category>> {
        return categoryDao.observeCategories().map { it.toCategory() }
    }

    override suspend fun insertCategory(category: Category) {
        categoryDao.upsertCategory(category.toCategoryEntity())
    }

    override suspend fun insertCategory(category: List<Category>) {
        categoryDao.upsertCategory(category.toCategoryEntity())
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category.toCategoryEntity())
    }

    override suspend fun getCategoryById(id: Int): Category {
        return categoryDao.getCategoryById(id).toCategory()
    }
}