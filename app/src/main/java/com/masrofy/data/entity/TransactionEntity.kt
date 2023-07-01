package com.masrofy.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.masrofy.mapper.toTransactions
import com.masrofy.model.Transaction
import com.masrofy.model.TransactionCategory
import com.masrofy.model.TransactionGroup
import com.masrofy.model.TransactionType
import com.masrofy.screens.mainScreen.CategoryWithAmount
import com.masrofy.utils.formatAsDisplayNormalize
import com.masrofy.utils.toMillis
import java.time.LocalDateTime
import java.time.ZoneOffset

@Entity
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transactionId")
    val transactionId: Int = 0,
    @ColumnInfo(name = "transactionAccountId")
    val accountTransactionId: Int,
    val transactionType: TransactionType,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val amount: Long,
    val comment: String? = null,
    val category: String
) {
    companion object {
        fun createTransaction(
            accountId: Int,
            transactionType: TransactionType,
            createdAt: LocalDateTime = LocalDateTime.now(),
            amount: Long,
            comment: String?,
            category: String
        ) = TransactionEntity(
            0,
            accountId,
            transactionType, createdAt, amount, comment, category
        )

        fun createTransactionWithId(
            transactionId: Int,
            accountId: Int,
            transactionType: TransactionType,
            createdAt: LocalDateTime = LocalDateTime.now(),
            amount: Long,
            comment: String?,
            category: String
        ) = TransactionEntity(
            transactionId,
            accountId,
            transactionType, createdAt, amount, comment, category
        )
    }
}

fun List<Transaction>.getCategoryWithAmount(): List<CategoryWithAmount> {
    val list = mutableListOf<CategoryWithAmount>()
    forEach { transaction ->
        val findCategory = list.find { it.category == transaction.category.toString() }
        if (findCategory != null) {
            val updateAmount = transaction.amount + findCategory.amount
            findCategory.amount = updateAmount
        } else {
            list.add(
                CategoryWithAmount(
                    transaction.category.toString(), transaction.amount
                )
            )
        }
    }
    return list
}

fun List<TransactionEntity>.toTransactionGroup(): List<TransactionGroup> {
    val getDates = map {
        LocalDateTime.ofEpochSecond(it.createdAt.toMillis(), 0, ZoneOffset.UTC).toLocalDate()
    }.toSet()
    val transactionGroup = mutableListOf<TransactionGroup>()

    getDates.forEach {
        val subLists = filter { transactionEntity ->
            transactionEntity.createdAt.toLocalDate().isEqual(it)
        }
        var income = 0f
        var expenve = 0f
        subLists.forEach {
            if (it.transactionType == TransactionType.INCOME) {
                income += it.amount
            } else if (it.transactionType == TransactionType.EXPENSE) {
                expenve += it.amount
            }
        }
        transactionGroup.add(
            TransactionGroup(
                subLists.toTransactions().sortedWith(compareByDescending {
                    it.createdAt
                }),
                it,
                formatAsDisplayNormalize(income.toBigDecimal()),
                formatAsDisplayNormalize(expenve.toBigDecimal())

            )
        )
    }
    return transactionGroup.apply {
        sortWith(compareByDescending {
            it.date
        })
    }
}



