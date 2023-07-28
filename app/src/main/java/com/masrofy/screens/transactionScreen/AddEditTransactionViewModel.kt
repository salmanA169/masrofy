package com.masrofy.screens.transactionScreen

import android.app.Activity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.AdsManager
import com.masrofy.Screens
import com.masrofy.TRANSACTION_ID
import com.masrofy.component.InputType
import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.data.entity.toAccount
import com.masrofy.model.TransactionType
import com.masrofy.model.getDefaultAccount
import com.masrofy.repository.AccountRepository
import com.masrofy.repository.TransactionRepository
import com.masrofy.repository.category_repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddEditTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val adsManager: AdsManager,
    private val dispatcherProvider: DispatcherProvider,
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val transactionId = savedStateHandle.get<Int>(TRANSACTION_ID)
    private val _transactionDetailState = MutableStateFlow(AddEditTransactionState())
    val transactionDetailState = _transactionDetailState.asStateFlow()

    private val _effect = MutableStateFlow<TransactionDetailEffect>(TransactionDetailEffect.Noting)
    val effect = _effect.asStateFlow()

    fun showAds(activity: Activity) {
        adsManager.showAds(activity)
    }

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            categoryRepository.observeCategories().collect { categories ->
                _transactionDetailState.update {
                    it.copy(
                        transactionCategories = categories.sortedWith(compareBy { if (it.position == 0) categories.size else it.position })
                    )
                }
            }
        }
        viewModelScope.launch(dispatcherProvider.io) {

            accountRepository.getAccounts().collect { accounts ->
                _transactionDetailState.update {
                    it.copy(
                        accounts = accounts,
                        selectedAccount = accounts.getDefaultAccount()
                    )
                }
            }
        }
        viewModelScope.launch(dispatcherProvider.io) {
            if (transactionId != -1 && transactionId != null) {
                val transaction = transactionRepository.getTransactionById(transactionId)
                val account = accountRepository.getAccountById(transaction.accountTransactionId)
                _transactionDetailState.update {
                    it.copy(
                        transactionId = transaction.transactionId,
                        totalAmount = transaction.amount.toString(),
                        transactionCategory = transaction.category,
                        selectedAccount = account.toAccount(emptyList()),
                        date = transaction.createdAt,
                        transactionType = transaction.transactionType,
                        isEdit = true,
                        comment = transaction.comment
                    )
                }

            }
        }
    }

    fun onEvent(event: AddEditTransactionEvent) {
        when (event) {
            is AddEditTransactionEvent.AccountSelected -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    val getAccountById = accountRepository.getAccountById(event.accountId)
                    _transactionDetailState.update {
                        it.copy(
                            selectedAccount = getAccountById.toAccount()
                        )
                    }
                }

            }

            is AddEditTransactionEvent.AmountChange -> {
                _transactionDetailState.update {
                    val formatText = event.text.replace("\\D".toRegex(), "")
                    it.copy(
                        totalAmount = formatText
                    )
                }
            }

            is AddEditTransactionEvent.CategorySelected -> {
                _transactionDetailState.update {
                    it.copy(
                        transactionCategory = event.transactionCategory
                    )
                }
            }

            is AddEditTransactionEvent.CommentChange -> {
                _transactionDetailState.update {
                    it.copy(
                        comment = event.comment
                    )
                }
            }

            is AddEditTransactionEvent.DateTimeChanged -> {
                _transactionDetailState.update {
                    it.copy(
                        date = event.date
                    )
                }
            }

            AddEditTransactionEvent.Delete -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    transactionRepository.deleteTransaction(_transactionDetailState.value.toTransactionEntityWithId())
                    _effect.value = TransactionDetailEffect.ClosePage
                }
            }

            AddEditTransactionEvent.Save -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    val transaction = _transactionDetailState.value
                    if (transaction.transactionCategory == null) {
                        _effect.update {
                            TransactionDetailEffect.ErrorMessage("You must choose category")
                        }
                    } else if (transaction.totalAmount.isEmpty() || transaction.totalAmount.toLong() > 99999999) {
                        _effect.update {
                            TransactionDetailEffect.ErrorMessage("The amount zero or greater then 99999")
                        }
                    } else {
                        if (transaction.isEdit!!) {
                            transactionRepository.updateTransaction(transaction.toTransactionEntityWithId())
                        } else {
                            transactionRepository.insertTransaction(transaction.toTransactionEntity())
                        }
                        _effect.value = TransactionDetailEffect.ClosePage
                    }
                }
            }

            is AddEditTransactionEvent.TransactionTypeChange -> {
                if (_transactionDetailState.value.transactionType != event.transactionType) {
                    _transactionDetailState.update {
                        it.copy(
                            transactionType = event.transactionType,
                            transactionCategory = null
                        )
                    }
                }
            }


            AddEditTransactionEvent.ClosePage -> {
                _effect.update {
                    TransactionDetailEffect.ClosePage
                }
            }

            is AddEditTransactionEvent.NavigateTo -> {
                val getTransactionType = _transactionDetailState.value.transactionType
                when (event.inputType) {
                    InputType.ACCOUNT_INPUT -> Unit
                    InputType.CATEGORY_INPUT -> {
                        _effect.update {
                            TransactionDetailEffect.Navigate(
                                Screens.CategoriesScreen.navigateToCategoriesWithArg(
                                    getTransactionType.name
                                )
                            )
                        }
                    }

                    InputType.KEYBOARD -> Unit
                    InputType.DATE_INPUT -> Unit
                }
            }
        }
    }

    fun resetEffect() {
        _effect.update {
            TransactionDetailEffect.Noting
        }
    }
}

sealed class TransactionDetailEffect() {
    object ClosePage : TransactionDetailEffect()
    class ErrorMessage(val message: String) : TransactionDetailEffect()
    class Navigate(val route: String) : TransactionDetailEffect()
    object Noting : TransactionDetailEffect()
}

sealed class AddEditTransactionEvent {
    class TransactionTypeChange(val transactionType: TransactionType) : AddEditTransactionEvent()
    class AmountChange(val text: String) : AddEditTransactionEvent()
    class AccountSelected(val accountId: Int) : AddEditTransactionEvent()
    class CategorySelected(val transactionCategory: String) : AddEditTransactionEvent()
    class DateTimeChanged(val date: LocalDateTime) : AddEditTransactionEvent()
    class CommentChange(val comment: String) : AddEditTransactionEvent()
    class NavigateTo(val inputType: InputType) : AddEditTransactionEvent()
    object Save : AddEditTransactionEvent()
    object Delete : AddEditTransactionEvent()
    object ClosePage : AddEditTransactionEvent()
}
