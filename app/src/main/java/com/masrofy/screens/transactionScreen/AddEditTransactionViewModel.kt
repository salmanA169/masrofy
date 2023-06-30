package com.masrofy.screens.transactionScreen

import android.app.Activity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.AdsManager
import com.masrofy.data.entity.toAccount
import com.masrofy.model.Account
import com.masrofy.model.TransactionCategory
import com.masrofy.model.TransactionType
import com.masrofy.model.getDefaultAccount
import com.masrofy.repository.AccountRepository
import com.masrofy.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val transactionId = savedStateHandle.get<Int>("transactionId")
    private val _transactionDetailState = MutableStateFlow(AddEditTransactionState())
    val transactionDetailState = _transactionDetailState.asStateFlow()

    private val _effect = MutableStateFlow<TransactionDetailEffect>(TransactionDetailEffect.Noting)
    val effect = _effect.asStateFlow()

    fun showAds(activity: Activity) {
        adsManager.showAds(activity)
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {

            accountRepository.getAccounts().collect { accounts ->
                _transactionDetailState.update {
                    it.copy(
                        accounts = accounts,
                        selectedAccount = accounts.getDefaultAccount()
                    )
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
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
                        comment = it.comment
                    )
                }

            }
        }
    }

    fun onEvent(event: AddEditTransactionEvent) {
        when (event) {
            is AddEditTransactionEvent.AccountSelected -> {
                _transactionDetailState.update {
                    it.copy(
                        selectedAccount = event.account
                    )
                }

            }

            is AddEditTransactionEvent.AmountChange -> {
                _transactionDetailState.update {
//                    val formatText = event.text.replace("\\D".toRegex(), "")
                    it.copy(
                        totalAmount = event.text
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
                viewModelScope.launch(Dispatchers.IO) {

                    transactionRepository.deleteTransaction(_transactionDetailState.value.toTransactionEntityWithId())
                    _effect.value = TransactionDetailEffect.ClosePage
                }
            }

            AddEditTransactionEvent.Save -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (_transactionDetailState.value.isValidToSave()) {
                        val transaction = _transactionDetailState.value
                        if (transaction.isEdit) {
                            transactionRepository.updateTransaction(transaction.toTransactionEntityWithId())
                        } else {
                            transactionRepository.insertTransaction(transaction.toTransactionEntity())
                        }

                        _effect.value = TransactionDetailEffect.ClosePage
                    } else {
                        _effect.update {
                            TransactionDetailEffect.ErrorMessage("The amount less or equal zero")
                        }
                    }
                }
            }

            is AddEditTransactionEvent.TransactionTypeChange -> {
                _transactionDetailState.update {
                    it.copy(
                        transactionType = event.transactionType
                    )
                }
            }

            AddEditTransactionEvent.ClosePage -> {
                _effect.update {
                    TransactionDetailEffect.ClosePage
                }
            }
        }
    }
}

sealed class TransactionDetailEffect() {
    object ClosePage : TransactionDetailEffect()
    class ErrorMessage(val message: String) : TransactionDetailEffect()
    object Noting : TransactionDetailEffect()
}

sealed class AddEditTransactionEvent {
    class TransactionTypeChange(val transactionType: TransactionType) : AddEditTransactionEvent()
    class AmountChange(val text: String) : AddEditTransactionEvent()
    class AccountSelected(val account: Account) : AddEditTransactionEvent()
    class CategorySelected(val transactionCategory: String) : AddEditTransactionEvent()
    class DateTimeChanged(val date: LocalDateTime) : AddEditTransactionEvent()
    class CommentChange(val comment: String) : AddEditTransactionEvent()
    object Save : AddEditTransactionEvent()
    object Delete : AddEditTransactionEvent()
    object ClosePage : AddEditTransactionEvent()
}
