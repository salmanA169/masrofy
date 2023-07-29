package com.masrofy

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.data.entity.CategoryEntity
import com.masrofy.mapper.toCategory
import com.masrofy.model.TransactionCategory
import com.masrofy.repository.category_repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val dispatcherProvider: DispatcherProvider
):ViewModel() {

    fun checkCategories(){
        viewModelScope.launch(dispatcherProvider.io) {
            // TODO: fix here if update app no categories inserted
            val getCategories = categoryRepository.getCategories()
            Log.d("MainViewModel", "checkCategories: ${getCategories.isEmpty()}")
            if (getCategories.isEmpty()){
                val toCategoryEntity = TransactionCategory.values().map {
                    CategoryEntity(0,it.nameCategory,it.type.name,true,it.position)
                }
                categoryRepository.insertCategory(toCategoryEntity.toCategory())
            }
        }
    }
}