package com.masrofy

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.masrofy.screens.categories.add_edit_category.addEditCategoryDest
import com.masrofy.screens.categories.categoriesDest
import com.masrofy.screens.currency.currencyScreen
import com.masrofy.screens.mainScreen.mainScreenNavigation
import com.masrofy.screens.onboarding.onBoardingDest
import com.masrofy.screens.settings.backups.backupScreens
import com.masrofy.screens.settings.backups.device_backup.deviceBackupDest
import com.masrofy.screens.settings.backups.drive_backup.driveBackupDest
import com.masrofy.screens.settings.settingsDest
import com.masrofy.screens.statisticsScreen.statisticsScreen
import com.masrofy.screens.top_transactions_details.topTransactionsDetailsDest
import com.masrofy.screens.transactionScreen.transactionScreenNavigation
import com.masrofy.screens.transactions_details.transactionsDetailsDest
import com.masrofy.ui.theme.MasrofyTheme
import com.masrofy.ui.theme.SurfaceColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)

    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel.checkCategories()
        viewModel.checkOnboarding()
        installSplashScreen()
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) {

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_MEDIA_LOCATION
                )
            )
        }

        setContent {
            MasrofyTheme() {
                // A surface container using the 'background' color from the theme
                SetNavigationScreen(viewModel)
            }
        }
    }

}


@Composable
fun SetNavigationScreen(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    val backStackEntries by navController.currentBackStackEntryAsState()
    val effect by mainViewModel.showOnboarding.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = effect) {
        when (effect) {
            is MainEffect.Navigate -> {
                navController.navigate((effect as MainEffect.Navigate).route) {
                    popUpTo(Screens.MainScreen.route) {
                        inclusive = true
                    }
                }
                mainViewModel.resetOnboardingValue()
            }

            null -> {

            }

            else -> {}
        }
    }
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
                BottomAppBar(containerColor = SurfaceColor.surfaces.surfaceContainer, actions = {
                    val rememberClick = remember {
                        {
                            navController.navigate(Screens.StatisticsScreen.route)
                        }
                    }
                    val rememberSettingClick = remember {
                        {
                            navController.navigate(Screens.Settings.route)
                        }
                    }
                    IconButton(onClick = rememberSettingClick) {
                        Icon(
                            rememberVectorPainter(image = Icons.Filled.Settings),
                            contentDescription = "",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = rememberClick) {
                        Icon(
                            rememberVectorPainter(image = ImageVector.vectorResource(id = R.drawable.statistic_icon1)),
                            contentDescription = "",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
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
                .fillMaxSize(),
            navController = navController,
            startDestination = Screens.MainScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(220, delayMillis = 90)
                ) + scaleIn(
                    initialScale = 0.92f,
                    animationSpec = tween(220, delayMillis = 90)
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(90))
            },
            popEnterTransition = {
                fadeIn(
                    animationSpec = tween(220, delayMillis = 90)
                ) + scaleIn(
                    initialScale = 0.92f,
                    animationSpec = tween(220, delayMillis = 90)
                )
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(90))
            }
        ) {
            mainScreenNavigation(navController, it)
            transactionScreenNavigation(navController)
            statisticsScreen(navController)
            transactionsDetailsDest(navController)
            topTransactionsDetailsDest(navController)
            categoriesDest(navController)
            addEditCategoryDest(navController)
            onBoardingDest(navController)
            settingsDest(navController)
            currencyScreen(navController)
            backupScreens(navController)
            driveBackupDest(navController)
            deviceBackupDest(navController)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, locale = "en")
@Composable
fun DefaultPreview() {
    MasrofyTheme {
        SetNavigationScreen(hiltViewModel())
    }
}