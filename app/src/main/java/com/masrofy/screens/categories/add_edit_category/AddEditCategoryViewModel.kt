package com.masrofy.screens.categories.add_edit_category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.CATEGORY_ID_ARG
import com.masrofy.TRANSACTION_TYPE_ARG
import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.Category
import com.masrofy.repository.TransactionRepository
import com.masrofy.repository.category_repository.CategoryRepository
import com.masrofy.screens.transactionScreen.AddEditTransactionEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditCategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val transactionRepository: TransactionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _categoryState = MutableStateFlow(AddEditCategoryState())
    val categoryState = _categoryState.asStateFlow()

    private val _categoryEffect =
        MutableStateFlow<AddEditCategoryEffect>(AddEditCategoryEffect.Nothing)
    val categoryEffect = _categoryEffect.asStateFlow()

    private val transactionType = savedStateHandle.get<String>(TRANSACTION_TYPE_ARG)!!
    private val categoryId = savedStateHandle.get<Int>(CATEGORY_ID_ARG)

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            if (categoryId != null && categoryId != -1) {
                val getCategory = categoryRepository.getCategoryById(categoryId)
                _categoryState.update {
                    it.copy(
                        category = getCategory.nameCategory
                    )
                }
            }
        }
    }

    fun onEvent(event: AddEditCategoryEvent) {
        when (event) {
            is AddEditCategoryEvent.OnChangeText -> {
                _categoryState.update {
                    it.copy(
                        event.text,
                        event.text.isEmpty(),
                        ""
                    )
                }
            }

            AddEditCategoryEvent.Save -> {
                val getState = _categoryState.value
                if (getState.category.isEmpty()) {
                    _categoryState.update {
                        it.copy(
                            showError = true,
                            errorMessage = "Can not be empty"
                        )
                    }
                } else {
                    viewModelScope.launch(dispatcherProvider.io) {
                        val category = getState.category
                        if (categoryId != null && categoryId != -1) {
                            val getCategoryById = categoryRepository.getCategoryById(categoryId)
                            categoryRepository.insertCategory(
                                Category(
                                    categoryId,
                                    category,
                                    transactionType,
                                    false,
                                    0
                                )
                            )
                            updateCategoryTransactions(getCategoryById.nameCategory, category)
                        } else {
                            categoryRepository.insertCategory(
                                Category(
                                    0,
                                    category,
                                    transactionType,
                                    false,
                                    0
                                )
                            )
                        }


                        _categoryEffect.update {
                            AddEditCategoryEffect.Saved
                        }
                    }
                }
            }
        }
    }

    private suspend fun updateCategoryTransactions(oldCategory: String, newCategory: String) {
        val getTransactions = transactionRepository.getTransactions().filter {
            it.category == oldCategory
        }.map {
            TransactionEntity.createTransactionWithId(
                it.transactionId,
                it.accountTransactionId,
                it.transactionType,
                it.createdAt,
                it.amount,
                it.comment,
                newCategory
            )
        }.forEach {
         transactionRepository.updateTransaction(it)
        }
    }
}

sealed interface AddEditCategoryEffect {
    object Nothing : AddEditCategoryEffect
    object Saved : AddEditCategoryEffect
}

sealed class AddEditCategoryEvent {
    class OnChangeText(val text: String) : AddEditCategoryEvent()
    object Save : AddEditCategoryEvent()
}