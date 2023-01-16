package com.masrofy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.masrofy.component.CustomScaffold
import com.masrofy.screens.mainScreen.mainScreenNavigation
import com.masrofy.screens.transactionScreen.transactionScreenNavigation
import com.masrofy.ui.theme.MasrofyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MasrofyTheme {
                // A surface container using the 'background' color from the theme
                SetNavigationScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetNavigationScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomAppBar(actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "add")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "add")

                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "add")

                }
            }, floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Screens.TransactionScreen.route) },

                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "")
                }
            })
        },
    ) {
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            navController = navController,
            startDestination = Screens.MainScreen.route
        ) {
            mainScreenNavigation(navController)
            transactionScreenNavigation(navController)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, locale = "en")
@Composable
fun DefaultPreview() {
    MasrofyTheme {
        SetNavigationScreen()
    }
}