package com.masrofy.screens.categories

import androidx.compose.runtime.Immutable
import com.masrofy.model.Category

@Immutable
data class CategoriesState(
    val categories:List<Category> = emptyList(),
    val title:String = ""
)