package com.masrofy

import com.google.common.truth.Truth
import com.masrofy.data.database.MasrofyDatabase
import com.masrofy.data.entity.*
import com.masrofy.model.CategoryAccount
import com.masrofy.model.TransactionCategory
import com.masrofy.model.TransactionType
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After

import org.junit.Test

import org.junit.Before
import org.junit.Rule
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class ExampleInstrumentedTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: MasrofyDatabase

    @Before
    fun setup(){
        hiltRule.inject()
    }
    @After
    fun finish(){
        database.close()
    }


    @Test
    fun testMigrationCategory(){
        runTest(StandardTestDispatcher()) {
            val data = TransactionCategory.values().map { CategoryEntity(0,it.name,it.type.name,true,0) }
            val getCategories = database.categoryDao.getCategories()
            Truth.assertThat(getCategories).isEqualTo(data)
        }
    }
    @Test
    fun testIfAccountAvailable(){
        runTest {
            val accounts = database.transactionDao.observeAccounts()
            val account = accounts.first()
            Truth.assertThat(account.hasDefaultAccount()).isTrue()
        }
    }

    @Test
    fun test_account_with_transactions(){
        runTest {
            val getDefaultAccount = database.transactionDao.observeAccounts().first().find {
                it.name == "Cash"
            }!!
            val newAccount = AccountEntity(0,"Salman",CategoryAccount.CASH,0, LocalDateTime.now(),"","")
            database.transactionDao.upsertAccount(newAccount)
            for (i in 1..10){
                database.transactionDao.insertTransaction(
                        TransactionEntity.createTransaction(
                            accountId = getDefaultAccount.accountId,
                            transactionType = TransactionType.EXPENSE,
                            amount = i * 2L,
                            category = TransactionCategory.FOOD.nameCategory,
                            comment = "", currencyCode = "", countryCode = ""
                    )
                )
            }
            for (i in 1..5){
                database.transactionDao.insertTransaction(
                    TransactionEntity.createTransaction(
                        accountId = newAccount.accountId,
                        transactionType = TransactionType.EXPENSE,
                        amount = i * 2L,
                        category = TransactionCategory.FOOD.nameCategory,
                        comment = "",
                        currencyCode = "", countryCode = ""
                    )
                )
            }
            val transactionsWithAccount = database.transactionDao.getAccountWithTransaction().first()
            println(transactionsWithAccount)
        }
    }
}