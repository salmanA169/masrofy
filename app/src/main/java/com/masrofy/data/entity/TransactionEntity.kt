package com.masrofy.data.entity

import androidx.compose.ui.text.toLowerCase
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.masrofy.model.TransactionCategory
import com.masrofy.model.TransactionGroup
import com.masrofy.model.TransactionType
import com.masrofy.screens.mainScreen.TransactionGroupUI
import com.masrofy.screens.mainScreen.TransactionItemState
import com.masrofy.utils.formatAsDisplayNormalize
import com.masrofy.utils.toMillis
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.random.Random

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
    val category: TransactionCategory
) {
    companion object {
        fun createTransaction(
            accountId: Int,
            transactionType: TransactionType,
            createdAt: LocalDateTime = LocalDateTime.now(),
            amount: Long,
            comment: String?,
            category: TransactionCategory
        ) = TransactionEntity(
            0,
            accountId,
            transactionType, createdAt, amount, comment, category
        )
        fun createTransactionWithId(transactionId: Int,
            accountId: Int,
            transactionType: TransactionType,
            createdAt: LocalDateTime = LocalDateTime.now(),
            amount: Long,
            comment: String?,
            category: TransactionCategory
        ) = TransactionEntity(
            transactionId,
            accountId,
            transactionType, createdAt, amount, comment, category
        )
    }
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
                subLists.sortedWith(compareByDescending{
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
fun List<TransactionEntity>.toTransactionGroupUI(): List<TransactionGroupUI> {
    val getDates = map {
        LocalDateTime.ofEpochSecond(it.createdAt.toMillis(), 0, ZoneOffset.UTC).toLocalDate()
    }.toSet()
    val transactionGroup = mutableListOf<TransactionGroupUI>()

    getDates.forEach {
        val subLists = filter { transactionEntity ->
            transactionEntity.createdAt.toLocalDate().isEqual(it)
        }
        val transactionItemState  = subLists.map {
            TransactionItemState(
                it.transactionId,
                it.category.icon,
                it.category.toString().toLowerCase(),
                it.comment,
                it.amount,
                it.transactionType
            )
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
            TransactionGroupUI(
                transactionItemState,
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