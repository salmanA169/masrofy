package com.masrofy

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.common.truth.Truth
import com.masrofy.data.database.MasrofyDatabase
import com.masrofy.data.entity.CategoryEntity
import com.masrofy.data.entity.TransactionEntity
import com.masrofy.di.DiAdsModule
import com.masrofy.di.DiModuleActivityScope
import com.masrofy.model.Transaction
import com.masrofy.model.TransactionType
import com.masrofy.repository.TransactionRepository
import com.masrofy.repository.category_repository.CategoryRepository
import com.masrofy.screens.mainScreen.MainScreen
import com.masrofy.screens.mainScreen.MainScreenState
import com.masrofy.screens.mainScreen.MainViewModel
import com.masrofy.ui.theme.MasrofyTheme
import com.masrofy.utils.createTestTransaction
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MainScreenTest {

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var database: MasrofyDatabase

    @Inject
    lateinit var categoryRepository: CategoryRepository

    @Inject
    lateinit var dispatcherProvider: TestDispatcherProvider
    @Inject
    lateinit var dataStore: DataStore<Preferences>
    @Inject
    lateinit var transactionRepo: TransactionRepository
    @Before
    fun setup() {
        hiltRule.inject()
//        composeTestRule.setContent {
//            MasrofyTheme {
//                SetNavigationScreen(
//                    com.masrofy.MainViewModel(categoryRepository,dispatcherProvider,dataStore,transactionRepo)
//                )
//            }
//        }
    }

    @Test
    fun testMainScreen() {
        // Start the app

        composeTestRule.onNodeWithContentDescription("add transaction").performClick()
    }
}