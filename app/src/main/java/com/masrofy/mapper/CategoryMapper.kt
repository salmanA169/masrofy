package com.masrofy.mapper

import com.masrofy.data.entity.CategoryEntity
import com.masrofy.model.Category


fun CategoryEntity.toCategory() = Category(nameCategory, type ,isPrimary)