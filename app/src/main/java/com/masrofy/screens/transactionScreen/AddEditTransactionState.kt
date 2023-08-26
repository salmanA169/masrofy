package com.masrofy.screens.transactionScreen

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.TextFieldValue
import com.masrofy.currency.Currency
import com.masrofy.currency.CurrencyData
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.Account
import com.masrofy.model.Category
import com.masrofy.model.TransactionCategory
import com.masrofy.model.TransactionType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Immutable
data class AddEditTransactionState(
    val transactionId:Int = -1,
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val totalAmount: String = "",
    val accounts: List<Account> = emptyList(),
    val selectedAccount: Account? = null,
    val transactionCategory: String? = null ,
    val date: LocalDateTime = LocalDateTime.now(),
    val comment: String? = null,
    val transactionCategories:List<Category> = listOf(),
    var isEdit: Boolean = false,
    val currency: Currency ? = null
) {
    fun isValidToSave(): Boolean {
        return totalAmount.isNotEmpty() && !totalAmount.contentEquals("0") && selectedAccount != null
    }

}

fun AddEditTransactionState.toTransactionEntity() = TransactionEntity.createTransaction(
    selectedAccount!!.id,
    transactionType,
    date,
    totalAmount.toLong(),
    comment,
    transactionCategory!!,
    currency!!.currencyCode, currency.countryCode
)

fun AddEditTransactionState.toTransactionEntityWithId() = TransactionEntity.createTransactionWithId(
    transactionId,
    selectedAccount!!.id,
    transactionType,
    date,
    totalAmount.toLong(),
    comment,
    transactionCategory!!,
    currency!!.currencyCode, currency.countryCode
)