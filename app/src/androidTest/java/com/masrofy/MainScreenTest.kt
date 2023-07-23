package com.masrofy

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.masrofy.di.DiAdsModule
import com.masrofy.di.DiModuleActivityScope
import com.masrofy.screens.mainScreen.MainScreen
import com.masrofy.screens.mainScreen.MainScreenState
import com.masrofy.screens.mainScreen.MainViewModel
import com.masrofy.ui.theme.MasrofyTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
@HiltAndroidTest
class MainScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup(){
        hiltRule.inject()
    }
    lateinit var mainViewModel :MainViewModel
    @Test
    fun testMainScreen() {
        // Start the app
        composeTestRule.setContent {
            MasrofyTheme {
                MainScreen(MainScreenState(), PaddingValues())
            }
        }
        val resource = composeTestRule.activity.getString(R.string.mybalance)
        composeTestRule.onNodeWithText(resource).assertExists()
    }
}