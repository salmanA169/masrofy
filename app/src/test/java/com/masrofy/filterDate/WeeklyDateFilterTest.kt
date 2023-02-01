package com.masrofy.filterDate

import com.google.common.collect.ImmutableMultimap
import com.google.common.truth.Truth
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.model.TransactionCategory
import com.masrofy.model.TransactionType
import com.masrofy.screens.mainScreen.DateEvent
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

internal class WeeklyDateFilterTest {

    private lateinit var transactionDateFilter:TransactionDateFilter
    private lateinit var transactionDateFilterMonthly:TransactionDateFilter
    @Before
    fun setUp() {
        val transactionList = buildList<TransactionEntity> {
            repeat(15){
                add(
                    TransactionEntity.createTransaction(
                        1,
                        TransactionType.EXPENSE,
                        LocalDateTime.of(LocalDate.of(2023,2,it+1), LocalTime.now()),
                        1450,
                        "$it",
                        TransactionCategory.CAR
                    )
                )
            }
            add(
                TransactionEntity.createTransaction(
                    1,
                    TransactionType.EXPENSE,
                    LocalDateTime.of(LocalDate.of(2023,2,1), LocalTime.now()),
                    1450,
                    "",
                    TransactionCategory.CAR
                )
            )
            add(
                TransactionEntity.createTransaction(
                    1,
                    TransactionType.EXPENSE,
                    LocalDateTime.of(LocalDate.of(2023,3,1), LocalTime.now()),
                    1450,
                    "",
                    TransactionCategory.CAR
                )
            )
            add(
                TransactionEntity.createTransaction(
                    1,
                    TransactionType.EXPENSE,
                    LocalDateTime.of(LocalDate.of(2023,3,1), LocalTime.now()),
                    1450,
                    "",
                    TransactionCategory.CAR
                )
            )
        }
        transactionDateFilter = WeeklyDateFilter(transactionList)
        transactionDateFilterMonthly = MonthlyDateFilter(transactionList)
    }


    @Test
    fun rangeFromJunToFebWeekly(){

    }

    @Test
    fun junToMars_Correct(){
        val data = transactionDateFilterMonthly.getTransactions()
        val junExpectedData = transactionDateFilterMonthly.getTransactions().filter {
            it.createdAt.monthValue == 1
        }
        Truth.assertThat(data).containsExactlyElementsIn(junExpectedData)


        transactionDateFilterMonthly.updateDate(DateEvent.PLUS,1)
        val expectedFeb = transactionDateFilterMonthly.getTransactions().filter {
            it.createdAt.monthValue == 2
        }

        Truth.assertThat(transactionDateFilterMonthly.getTransactions()).containsExactlyElementsIn(expectedFeb)
        transactionDateFilterMonthly.updateDate(DateEvent.PLUS,1)
        val expectedMar = transactionDateFilterMonthly.getTransactions().filter {
            it.createdAt.monthValue == 3
        }
        Truth.assertThat(transactionDateFilterMonthly.getTransactions()).containsExactlyElementsIn(expectedMar)

    }


    @Test
    fun rangeDateFrom12To19_correct(){
        transactionDateFilter.updateDate(2023,1,12)
        Truth.assertThat(transactionDateFilter.getTransactions()).hasSize(4)
    }
    @Test
    fun RangeDateFrom14To21_Correct(){
        transactionDateFilter.updateDate(2023,1,14)
        val data = transactionDateFilter.getTransactions()
        val expectedData = data.filter {
            it.createdAt.dayOfMonth in 14..21
        }

        Truth.assertThat(data).containsExactlyElementsIn(expectedData)
//        println(data)
    }
    @Test
    fun rangeDateFrom14To21_Incorrect(){
        transactionDateFilter.updateDate(2023,1,14)
        val data = transactionDateFilter.getTransactions()
        val expectedData = data.filter {
            it.createdAt.dayOfMonth in 14..24
        }

        Truth.assertThat(data).containsExactlyElementsIn(expectedData)
//        println(data)
    }
}