package com.masrofy.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.masrofy.model.Account
import com.masrofy.model.CategoryAccount
import java.time.LocalDateTime

@Entity
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "accountId")
    val accountId: Int,
    val name: String,
    val type: CategoryAccount,
    val totalAmount: Long,
    val createdAt: LocalDateTime,
)


class AccountException(message:String):Exception(message)

// TODO: fix it later when first install it crash
fun List<AccountEntity>.getDefaultAccount() = find { it.name == "Cash" }?: throw AccountException("No Default account found")

fun List<AccountEntity>.hasDefaultAccount():Boolean  = if (isNotEmpty()){any {
    it.name == "Cash"
}} else false

fun defaultAccount() = AccountEntity(0, "Cash", CategoryAccount.CASH, 0, LocalDateTime.now())
fun AccountEntity.toAccount(transactions: List<TransactionEntity>) = Account(
    accountId,
    name,
    type, totalAmount, createdAt, transactions
)