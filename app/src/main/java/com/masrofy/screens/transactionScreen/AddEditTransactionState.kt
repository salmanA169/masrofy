package com.masrofy.screens.transactionScreen

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.TextFieldValue
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.Account
import com.masrofy.model.TransactionCategory
import com.masrofy.model.TransactionType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Immutable
data class AddEditTransactionState(
    val transactionId:Int = -1,
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val totalAmount: TextFieldValue = TextFieldValue(),
    val accounts: List<Account> = emptyList(),
    val selectedAccount: Account? = null,
    val transactionCategory: TransactionCategory = TransactionCategory.CAR,
    val date: LocalDate = LocalDate.now(),
    val comment: TextFieldValue? = null,
    var isEdit: Boolean = false
) {
    fun isValidToSave(): Boolean {
        return totalAmount.text.isNotEmpty() && !totalAmount.text.contentEquals("0") && selectedAccount != null
    }

}

fun AddEditTransactionState.toTransactionEntity() = TransactionEntity.createTransaction(
    selectedAccount!!.id,
    transactionType,
    LocalDateTime.of(date, LocalTime.now()),
    totalAmount.text.toLong(),
    comment?.text,
    transactionCategory
)

fun AddEditTransactionState.toTransactionEntityWithId() = TransactionEntity.createTransactionWithId(
    transactionId,
    selectedAccount!!.id,
    transactionType,
    LocalDateTime.of(date, LocalTime.now()),
    totalAmount.text.toLong(),
    comment?.text,
    transactionCategory
)