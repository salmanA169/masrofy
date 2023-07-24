package com.masrofy.mapper

import com.masrofy.data.entity.CategoryEntity
import com.masrofy.model.Category


fun CategoryEntity.toCategory() = Category(nameCategory, type ,isPrimary)
fun List<CategoryEntity>.toCategory () = map { it.toCategory() }

fun Category.toCategoryEntity() = CategoryEntity(nameCategory, type ,isPrimary)
fun List<Category>.toCategoryEntity () = map { it.toCategoryEntity() }