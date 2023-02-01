package com.masrofy.screens.statisticsScreen

import com.google.common.truth.Truth
import com.masrofy.repository.FakeRepository
import com.masrofy.screens.mainScreen.DateEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class StatisticViewModelTest {

    private val fakeRepository = FakeRepository()
    private lateinit var statisticViewModel: StatisticViewModel

    @Before
    fun setUp() {
        statisticViewModel = StatisticViewModel(fakeRepository)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun rangeDate() {
        runTest {
            val getStatistic = statisticViewModel.statisticState.first()
            val data = getStatistic.dataEntry
            val amount = getStatistic.totalExpense
            Truth.assertThat(amount).isEqualTo("224.50")
        }
    }
}