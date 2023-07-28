package com.masrofy.mapper

import com.masrofy.data.entity.CategoryEntity
import com.masrofy.model.Category


fun CategoryEntity.toCategory() = Category(idCategory,nameCategory, type ,isPrimary,position)
fun List<CategoryEntity>.toCategory () = map { it.toCategory() }

fun Category.toCategoryEntity() = CategoryEntity(id,nameCategory, type ,isPrimary,position)
fun List<Category>.toCategoryEntity () = map { it.toCategoryEntity() }