package com.masrofy.screens.transactionScreen

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.TextFieldValue
import com.masrofy.model.Account
import com.masrofy.model.TransactionCategory
import com.masrofy.model.TransactionType
import java.time.LocalDate

@Immutable
data class TransactionDetailsState(
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val totalAmount: TextFieldValue = TextFieldValue(),
    val accounts: List<Account> = emptyList(),
    val selectedAccount: Account? = null,
    val transactionCategory: TransactionCategory = TransactionCategory.CAR,
    val date: LocalDate = LocalDate.now(),
    val comment: String? = null,
    var isEdit: Boolean = false
) {
    fun isValidToSave(): Boolean {
        return totalAmount.text.isNotEmpty() && totalAmount.text.contentEquals("0") && selectedAccount != null
    }
}