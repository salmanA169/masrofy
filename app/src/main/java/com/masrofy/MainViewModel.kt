package com.masrofy

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.data.entity.CategoryEntity
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.data.entity.defaultAccount
import com.masrofy.data.entity.toAccount
import com.masrofy.mapper.toCategory
import com.masrofy.model.TransactionCategory
import com.masrofy.model.TransactionType
import com.masrofy.onboarding.OnboardingScreensConst
import com.masrofy.repository.TransactionRepository
import com.masrofy.repository.category_repository.CategoryRepository
import com.masrofy.utils.getDarkModeFlow
import com.masrofy.utils.getOnboardingIsFirstTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val dataStore: DataStore<Preferences>,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _showOnboarding = MutableStateFlow<MainEffect?>(null)
    val showOnboarding = _showOnboarding.asStateFlow()

    val isDarkMode = dataStore.getDarkModeFlow()
    private fun checkIfDefaultAccount() {
        viewModelScope.launch(dispatcherProvider.io) {
            val defaultAccount = defaultAccount()
            val accounts = transactionRepository.getAccount()
            if (accounts.find { it.name == defaultAccount.name } == null) {
                transactionRepository.upsertAccount(defaultAccount.toAccount())
            }


        }

    }

    init {
        checkIfDefaultAccount()
    }

    // for test
    fun saveTest() {
        viewModelScope.launch(dispatcherProvider.io) {

            transactionRepository.insertTransaction(
                TransactionEntity.createTransaction(
                    1,
                    TransactionType.EXPENSE,
                    amount = 500,
                    currencyCode = "USD",
                    countryCode = "US",
                    category = "Test",
                    comment = null
                )
            )
        }
    }

    fun checkOnboarding() {
        viewModelScope.launch(dispatcherProvider.io) {
            _showOnboarding.update {
                val isFirstTime = dataStore.getOnboardingIsFirstTime()
                if (isFirstTime) {
                    MainEffect.Navigate(
                        Screens.OnboardingScreen.navigateToOnboardingWithArg(
                            isFirstTime = true
                        )
                    )
                } else {
                    val filterList = OnboardingScreensConst.values().filter { onboarding ->
                        dataStore.data.map {
                            if (onboarding.keyStore == null) false else it[onboarding.keyStore]
                                ?: true
                        }.first()
                    }.map {
                        it.type
                    }
                    if (filterList.isNotEmpty()) {
                        MainEffect.Navigate(
                            Screens.OnboardingScreen.navigateToOnboardingWithArg(
                                filterList,
                                false
                            )
                        )
                    } else {
                        null
                    }
                }
            }
        }
    }

    fun resetOnboardingValue() {
        _showOnboarding.update {
            null
        }
    }

    fun checkCategories() {
        viewModelScope.launch(dispatcherProvider.io) {
            val getCategories = categoryRepository.getCategories()
            if (getCategories.isEmpty()) {
                val toCategoryEntity = TransactionCategory.values().map {
                    CategoryEntity(0, it.nameCategory, it.type.name, true, it.position)
                }
                categoryRepository.insertCategory(toCategoryEntity.toCategory())
            }
        }
    }
}

sealed class MainEffect {
    class Navigate(val route: String) : MainEffect()
}
