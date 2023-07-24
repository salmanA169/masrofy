package com.masrofy.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.masrofy.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Upsert
    suspend fun upsertCategory(categoryEntity: CategoryEntity)

    @Upsert
    suspend fun upsertCategory(categoryEntity: List<CategoryEntity>)

    @Query("SELECT * FROM CategoryEntity")
    fun observeCategories():Flow<List<CategoryEntity>>

    @Query("SELECT * FROM CategoryEntity")
    suspend fun getCategories():List<CategoryEntity>
}