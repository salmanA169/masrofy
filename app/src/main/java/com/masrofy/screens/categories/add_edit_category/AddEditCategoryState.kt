package com.masrofy.screens.categories.add_edit_category

data class AddEditCategoryState(
    val category:String = "",
    val showError:Boolean = false,
    val errorMessage:String = "",
    val transactionTypeTitle:String = ""
)
