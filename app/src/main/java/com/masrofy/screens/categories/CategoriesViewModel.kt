package com.masrofy.screens.categories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.Screens
import com.masrofy.TRANSACTION_TYPE_ARG
import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.repository.category_repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val dispatcherProvider:DispatcherProvider,
    savedStateHandle: SavedStateHandle
):ViewModel() {
    private val _categoryState = MutableStateFlow(CategoriesState())
    val categoryState = _categoryState.asStateFlow()

    private val transactionType = savedStateHandle.get<String>(TRANSACTION_TYPE_ARG)!!

    private val _categoryEffect = MutableStateFlow<CategoryEffect>(CategoryEffect.Nothing)
    val categoryEffect = _categoryEffect.asStateFlow()
    fun onEvent(event: CategoryEvent){
        when(event){
            is CategoryEvent.OnDelete -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    val getCategory = categoryRepository.getCategoryById(event.id)
                    categoryRepository.deleteCategory(getCategory)
                }
            }
             is CategoryEvent.NavigateToAddEditCategory -> {
                _categoryEffect.update {
                    CategoryEffect.OnNavigate(Screens.AddEditCategoryScreen.navigateToAddEditCategoriesWithArg(transactionType,event.id.toString()))
                }
            }
            is CategoryEvent.OnSwipe -> TODO()
            CategoryEvent.PopBack -> {
                _categoryEffect.update {
                    CategoryEffect.OnPopBack
                }
            }
        }
    }
    fun resetEffect(){
        _categoryEffect.update {
            CategoryEffect.Nothing
        }
    }
    init {
        viewModelScope.launch(dispatcherProvider.io) {
            categoryRepository.observeCategories().collect{
                val filterCategories = it.filter { it.type == transactionType }
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
sealed class CategoryEffect{
    class OnNavigate(val route:String):CategoryEffect()
    object OnPopBack:CategoryEffect()
    object Nothing:CategoryEffect()
}
sealed class CategoryEvent{
    class NavigateToAddEditCategory(val id:Int):CategoryEvent()
    class OnDelete(val id:Int):CategoryEvent()
    class OnSwipe(val position:Int,val id:Int):CategoryEvent()
    object PopBack:CategoryEvent()
}