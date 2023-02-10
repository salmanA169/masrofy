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
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TransactionDetailsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val adsManager: AdsManager,
    savedStateHandle: SavedStateHandle
) :ViewModel(){

    val transactionId = savedStateHandle.get<Int>("transactionId")
    private val _transactionDetailState = MutableStateFlow(TransactionDetailsState())
    val transactionDetailState = _transactionDetailState.asStateFlow()

    private val _effect = MutableStateFlow<TransactionDetailEffect>(TransactionDetailEffect.Noting)
    val effect = _effect.asStateFlow()

     fun showAds(activity:Activity){
        adsManager.showAds(activity)
    }
    init {
        viewModelScope.launch(Dispatchers.IO) {

            accountRepository.getAccounts().collect{accounts->
                _transactionDetailState.update {
                    it.copy(
                        accounts = accounts,
                        selectedAccount = accounts.getDefaultAccount()
                    )
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            if (transactionId !=-1 && transactionId!=null){
                val transaction = transactionRepository.getTransactionById(transactionId)
                val account = accountRepository.getAccountById(transaction.accountTransactionId)
                _transactionDetailState.update {
                    it.copy(
                        transactionId = transaction.transactionId,
                        totalAmount = it.totalAmount.copy(text = transaction.amount.toString()),
                        transactionCategory = transaction.category,
                        selectedAccount = account.toAccount(emptyList()),
                        date = transaction.createdAt.toLocalDate(),
                        transactionType = transaction.transactionType,
                        isEdit = true,
                        comment = it.comment?.copy(transaction.comment?:"")
                    )
                }

            }
        }
    }

    fun onEvent(event:TransactionDetailEvent){
        when(event){
            is TransactionDetailEvent.AccountSelected -> {
                _transactionDetailState.update {
                    it.copy(
                        selectedAccount = event.account
                    )
                }

            }
            is TransactionDetailEvent.AmountChange -> {
                _transactionDetailState.update {
                    val formatText = event.text.text.replace("\\D".toRegex(), "")
                    it.copy(
                        totalAmount = event.text.copy(formatText)
                    )
                }
            }
            is TransactionDetailEvent.CategorySelected -> {
                _transactionDetailState.update {
                    it.copy(
                        transactionCategory = event.transactionCategory
                    )
                }
            }
            is TransactionDetailEvent.CommentChange -> {
                _transactionDetailState.update {
                    it.copy(
                        comment = event.comment
                    )
                }
            }
            is TransactionDetailEvent.DateChanged -> {
                _transactionDetailState.update {
                    it.copy(
                        date = event.date
                    )
                }
            }

            TransactionDetailEvent.Delete -> {
                viewModelScope.launch(Dispatchers.IO) {

                    transactionRepository.deleteTransaction(_transactionDetailState.value.toTransactionEntityWithId())
                    _effect.value = TransactionDetailEffect.ClosePage
                }
            }
            TransactionDetailEvent.Save -> {
                viewModelScope.launch (Dispatchers.IO){
                    if (_transactionDetailState.value.isValidToSave()){
                        val transaction = _transactionDetailState.value
                        if (transaction.isEdit){
                            transactionRepository.updateTransaction(transaction.toTransactionEntityWithId())
                        }else{
                            transactionRepository.insertTransaction(transaction.toTransactionEntity())
                        }

                        _effect.value = TransactionDetailEffect.ClosePage
                    }else{
                        _effect.update {
                            TransactionDetailEffect.ErrorMessage("The amount less or equal zero")
                        }
                    }
                }
            }
            is TransactionDetailEvent.TransactionTypeChange -> {
                _transactionDetailState.update {
                    it.copy(
                        transactionType = event.transactionType
                    )
                }
            }

        }
    }
}
sealed class TransactionDetailEffect(){
    object ClosePage:TransactionDetailEffect()
    class ErrorMessage(val message:String):TransactionDetailEffect()
    object Noting:TransactionDetailEffect()

}
sealed class TransactionDetailEvent{

    class TransactionTypeChange(val transactionType:TransactionType):TransactionDetailEvent()
    class AmountChange(val text:TextFieldValue):TransactionDetailEvent()
    class AccountSelected(val account:Account):TransactionDetailEvent()
    class CategorySelected(val transactionCategory: TransactionCategory):TransactionDetailEvent()
    class DateChanged(val date:LocalDate):TransactionDetailEvent()

    class CommentChange(val comment:TextFieldValue):TransactionDetailEvent()

    object Save:TransactionDetailEvent()
    object Delete:TransactionDetailEvent()


}
