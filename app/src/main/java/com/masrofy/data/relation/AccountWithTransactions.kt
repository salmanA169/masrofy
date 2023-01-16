package com.masrofy.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.masrofy.data.entity.AccountEntity
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.Account
import com.masrofy.model.BalanceManager
import com.masrofy.model.TransactionType

data class AccountWithTransactions(
    @Embedded val account: AccountEntity,
    @Relation(
        parentColumn = "accountId",
        entityColumn = "transactionAccountId"
    )
    val transactions:List<TransactionEntity>
)
fun List<AccountWithTransactions>.toAccount() = map {
    it.account
}
fun List<AccountWithTransactions>.toTransactions() :List<TransactionEntity>{
    return buildList {
        this@toTransactions.forEach {
            addAll(it.transactions)
        }
    }
}
fun List<AccountWithTransactions>.toBalance():BalanceManager{

    var totalIncome = 0f
    var totalExpense = 0f
    forEach {
        it.transactions.forEach {transaction->
            totalIncome += if (transaction.transactionType == TransactionType.INCOME) transaction.amount else 0f
            totalExpense += if (transaction.transactionType == TransactionType.EXPENSE) transaction.amount else 0f
        }
    }
    return BalanceManager(
        totalAmount = totalIncome.toBigDecimal() - totalExpense.toBigDecimal(),
        totalIncome = totalIncome.toBigDecimal(),
        totalExpense = totalExpense.toBigDecimal()
    )
}