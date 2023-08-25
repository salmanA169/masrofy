package com.masrofy.model

import com.masrofy.currency.Currency
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.data.entity.defaultAccount
import java.time.LocalDateTime

data class Account(
    val id :Int,
    val name:String,
    val type: CategoryAccount,
    val totalAmount:Long,
    val createdAt:LocalDateTime,
    val transactions:List<Transaction> = emptyList(),
    val currency:Currency
)

fun List<Account>.getDefaultAccount() = find { it.name == defaultAccount().name }