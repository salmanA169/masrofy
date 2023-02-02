package com.masrofy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.masrofy.screens.statisticsScreen.statisticsScreen
import com.masrofy.screens.transactionScreen.transactionScreenNavigation
import com.masrofy.ui.theme.MasrofyTheme
import dagger.hilt.android.AndroidEntryPoint
import com.masrofy.screens.mainScreen.mainScreenNavigation

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MasrofyTheme {
                // A surface container using the 'background' color from the theme
                SetNavigationScreen()
            }
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun SetNavigationScreen() {
    val navController = rememberNavController()
    val backStackEntries by navController.currentBackStackEntryAsState()

    var showBottom by remember {
        mutableStateOf(true)
    }
    backStackEntries?.let { backStack ->
        when (backStack.destination.route) {
            Screens.MainScreen.route -> {
                showBottom = true
            }
            else -> {
                showBottom = false
            }
        }
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(visible = showBottom, enter = fadeIn(), exit = fadeOut()) {
                BottomAppBar(actions = {
                    val rememberClick = remember {
                        {
                            navController.navigate(Screens.StatisticsScreen.route)
                        }
                    }
                    IconButton(onClick = rememberClick) {
                        Icon(
                            rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.statistic_icon)),
                            contentDescription = "",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }, floatingActionButton = {
                    val rememberClick = remember {
                        {
                            navController.navigate(Screens.TransactionScreen.route + "/-1")
                        }
                    }
                    FloatingActionButton(
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 0.dp
                        ),
                        onClick = rememberClick,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "add transaction"
                        )
                    }
                })
            }

        },


        ) {

        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .semantics {
                    testTagsAsResourceId = true
                },
            navController = navController,
            startDestination = Screens.MainScreen.route
        ) {
            mainScreenNavigation(navController, it)
            transactionScreenNavigation(navController)
            statisticsScreen(navController)
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