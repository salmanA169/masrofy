package com.masrofy

import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.masrofy.screens.mainScreen.MainScreen
import com.masrofy.screens.mainScreen.MainScreenState
import com.masrofy.ui.theme.MasrofyTheme
import com.masrofy.utils.createTestTransaction
import com.masrofy.utils.onBoardingScreenIsFirstTime
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class NavigationTest {

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    lateinit var controller: NavController

    @Before
    fun setUp() {
        hiltRule.inject()
//        composeTestRule.activity.setContent {
//            controller = TestNavHostController(LocalContext.current)
//            controller.navigatorProvider.addNavigator(ComposeNavigator())
//            val mainViewModel = hiltViewModel<MainViewModel>()
////            mainViewModel.checkOnboarding()
//            MasrofyTheme {
//                SetNavigationScreen(
//                    mainViewModel,
//                )
//            }
//        }

    }
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun hasTransactionAndClick(){

        composeTestRule.onNodeWithContentDescription("add transaction").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasText("Cash"))
        composeTestRule.onNodeWithText("Category").performClick()
        composeTestRule.onNodeWithText("Food").performClick()
        composeTestRule.onNodeWithText("Amount").performTextInput("15000")
        composeTestRule.onNodeWithText("Save").performClick()
    }

    @Test
    fun addNewTransactionWithComment(){
        composeTestRule.onNodeWithContentDescription("add transaction").performClick()

        composeTestRule.onNodeWithText("Category").performClick()
        composeTestRule.onNodeWithText("Food").performClick()
        composeTestRule.onNodeWithText("Amount").performTextInput("15000")
        composeTestRule.onNodeWithText("Comment").performClick().performTextInput("test")
        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule.onNodeWithText("test").assertExists()
    }

    @Test
    fun addNewCategoryAndCheck(){
        composeTestRule.onNodeWithContentDescription("add transaction").performClick()
        composeTestRule.onNodeWithText("Category").performClick()
        composeTestRule.onNodeWithContentDescription("add category screen").performClick()
        composeTestRule.onNodeWithContentDescription("add category").performClick()
        composeTestRule.onNodeWithTag("edit category").performTextInput("Gas")
        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule.onNodeWithTag("list categories").performScrollToNode(hasText("Gas"))


    }

    @Test
    fun addNewCategoryAndDelete(){
        composeTestRule.onNodeWithContentDescription("add transaction").performClick()
        composeTestRule.onNodeWithText("Category").performClick()
        composeTestRule.onNodeWithContentDescription("add category screen").performClick()
        composeTestRule.onNodeWithContentDescription("add category").performClick()
        composeTestRule.onNodeWithTag("edit category").performTextInput("Gas")
        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule.onNodeWithTag("list categories").performScrollToNode(hasText("Gas") )
        composeTestRule.onNodeWithText("Gas").assertExists()
        composeTestRule.onNodeWithContentDescription("Gas").performClick()

        composeTestRule.onNodeWithText("Confirm").performClick()
        composeTestRule.onNodeWithText("Gas").assertDoesNotExist()


    }
}