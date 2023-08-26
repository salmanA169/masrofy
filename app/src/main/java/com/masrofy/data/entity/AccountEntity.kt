package com.masrofy.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.masrofy.currency.Currency
import com.masrofy.model.Account
import com.masrofy.model.CategoryAccount
import com.masrofy.model.Transaction
import java.time.LocalDateTime
import java.util.Locale

@Entity
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "accountId")
    val accountId: Int,
    val name: String,
    val type: CategoryAccount,
    val totalAmount: Long,
    val createdAt: LocalDateTime,
    @ColumnInfo(defaultValue = "USD",name = "currency-code")
    val currencyCode: String,
    @ColumnInfo(defaultValue = "US", name = "country-code")
    val countryCode:String
)


class AccountException(message: String) : Exception(message)

// TODO: fix it later when first install it crash
fun List<AccountEntity>.getDefaultAccount() =
    find { it.name == "Cash" } ?: throw AccountException("No Default account found")

fun List<AccountEntity>.hasDefaultAccount(): Boolean = if (isNotEmpty()) {
    any {
        it.name == "Cash"
    }
} else false

fun defaultAccount() = AccountEntity(
    0,
    "Cash",
    CategoryAccount.CASH,
    0,
    LocalDateTime.now(),
    "USD","US"
)

fun AccountEntity.toAccount(transactions: List<Transaction> = emptyList()) = Account(
    accountId,
    name,
    type, totalAmount, createdAt, transactions, Currency(currencyCode,countryCode)
)
