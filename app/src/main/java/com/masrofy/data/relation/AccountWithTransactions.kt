package com.masrofy.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.masrofy.data.entity.AccountEntity
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.data.entity.toAccount
import com.masrofy.model.BalanceManager
import com.masrofy.model.TransactionType
import com.masrofy.utils.formatAsDisplayNormalize

data class AccountWithTransactions(
    @Embedded val account: AccountEntity,
    @Relation(
        parentColumn = "accountId",
        entityColumn = "transactionAccountId"
    )
    val transactions:List<TransactionEntity>
)

fun List<AccountWithTransactions>.toAccount() = map {
    it.account.toAccount(it.transactions)
}

fun List<AccountWithTransactions>.toTransactions() :List<TransactionEntity>{
    return buildList {
        this@toTransactions.forEach {
            addAll(it.transactions)
        }
    }
}

fun List<TransactionEntity>.transactionsToBalance():BalanceManager{

    var totalIncome = 0L
    var totalExpense = 0L
    forEach {
            totalIncome += if (it.transactionType == TransactionType.INCOME) it.amount else 0
            totalExpense += if (it.transactionType == TransactionType.EXPENSE) it.amount else 0

    }
    return BalanceManager(
        totalAmount = formatAsDisplayNormalize((totalIncome - totalExpense).toBigDecimal())  ,
        totalIncome = formatAsDisplayNormalize(totalIncome.toBigDecimal()),
        totalExpense = formatAsDisplayNormalize(totalExpense.toBigDecimal())
    )
}
fun List<AccountWithTransactions>.toBalance():BalanceManager{

    var totalIncome = 0L
    var totalExpense = 0L
    forEach {
        it.transactions.forEach {transaction->
            totalIncome += if (transaction.transactionType == TransactionType.INCOME) transaction.amount else 0
            totalExpense += if (transaction.transactionType == TransactionType.EXPENSE) transaction.amount else 0
        }
    }
    return BalanceManager(
        totalAmount = formatAsDisplayNormalize((totalIncome - totalExpense).toBigDecimal())  ,
        totalIncome = formatAsDisplayNormalize(totalIncome.toBigDecimal()),
        totalExpense = formatAsDisplayNormalize(totalExpense.toBigDecimal())
    )
}