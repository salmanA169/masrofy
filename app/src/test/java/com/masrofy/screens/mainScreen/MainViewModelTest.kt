package com.masrofy.screens.mainScreen

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test

class MainViewModelTest {


    @Test
    fun getTransactionGroup() {
        val mainViewModel = MainViewModel()
        runBlocking {
            val groupTransaction = mainViewModel.transactionGroup.first()
            println(groupTransaction)
        }
    }
}