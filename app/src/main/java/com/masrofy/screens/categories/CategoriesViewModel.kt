package com.masrofy.screens.categories

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.Screens
import com.masrofy.TRANSACTION_TYPE_ARG
import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.model.Category
import com.masrofy.repository.category_repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _categoryState = MutableStateFlow(CategoriesState())
    val categoryState = _categoryState.asStateFlow()

    private val transactionType = savedStateHandle.get<String>(TRANSACTION_TYPE_ARG)!!

    private val _categoryEffect = MutableStateFlow<CategoryEffect>(CategoryEffect.Nothing)
    private var tempList = emptyList<Category>()
    val categoryEffect = _categoryEffect.asStateFlow()
    fun onEvent(event: CategoryEvent) {
        when (event) {
            is CategoryEvent.OnDelete -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    val getCategory = categoryRepository.getCategoryById(event.id)
                    categoryRepository.deleteCategory(getCategory)
                }
            }

            is CategoryEvent.NavigateToAddEditCategory -> {
                _categoryEffect.update {
                    CategoryEffect.OnNavigate(
                        Screens.AddEditCategoryScreen.navigateToAddEditCategoriesWithArg(
                            transactionType,
                            event.id.toString()
                        )
                    )
                }
            }

            is CategoryEvent.OnSwipe -> {
                tempList = tempList.toMutableList().apply {
                    add(event.to, removeAt(event.from))
                }
                _categoryState.update {
                    it.copy(
                        tempList
                    )
                }
            }

            CategoryEvent.PopBack -> {
                _categoryEffect.update {
                    CategoryEffect.OnPopBack
                }
            }

            is CategoryEvent.OnDragEnd -> {
                viewModelScope.launch(dispatcherProvider.io) {
//                    val tempMList = tempList.toMutableList()
//                    for(i in event.index..tempList.lastIndex){
//                        tempMList.get(i).apply {
//                            position = i +1
//                        }
//                    }
//                    tempMList.get(event.fromIndex).position = event.index+1
//                    categoryRepository.insertCategory(
//                        tempMList
//                    )
                }
            }
        }
    }

    fun resetEffect() {
        _categoryEffect.update {
            CategoryEffect.Nothing
        }
    }

    override fun onCleared() {
        super.onCleared()
        GlobalScope.launch(dispatcherProvider.io){
            val categoryEntity = tempList.onEachIndexed { index, category ->
                category.position = index + 1
            }
            categoryRepository.insertCategory(categoryEntity)
        }
    }
    init {
        viewModelScope.launch(dispatcherProvider.io) {
            categoryRepository.observeCategories().collect { categories ->
                val filterCategories =
                    categories.filter { it.type == transactionType }
                        .sortedWith(compareBy { if (it.position == 0) categories.size else it.position })
                tempList = filterCategories
                _categoryState.update {
                    it.copy(
                        categories = filterCategories,
                        title = transactionType
                    )
                }
            }
        }
    }
}

sealed class CategoryEffect {
    class OnNavigate(val route: String) : CategoryEffect()
    object OnPopBack : CategoryEffect()
    object Nothing : CategoryEffect()
}

sealed class CategoryEvent {
    class NavigateToAddEditCategory(val id: Int) : CategoryEvent()
    class OnDelete(val id: Int) : CategoryEvent()
    class OnSwipe(val from: Int, val to: Int) : CategoryEvent()
    class OnDragEnd(val fromIndex: Int, val index: Int) : CategoryEvent()
    object PopBack : CategoryEvent()
}