package com.masrofy.screens.settings

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masrofy.R
import com.masrofy.Screens
import com.masrofy.component.AppBar
import com.masrofy.component.SettingsComponent
import com.masrofy.component.SettingsSwitchCard
import com.masrofy.component.translatableRes
import com.masrofy.ui.theme.MasrofyTheme


fun NavGraphBuilder.settingsDest(navController: NavController) {
    composable(Screens.Settings.route) {
        val settingsViewModel = hiltViewModel<SettingsViewModel>()
        val state by settingsViewModel.state.collectAsStateWithLifecycle()
        val effect by settingsViewModel.effect.collectAsStateWithLifecycle()
        LaunchedEffect(key1 = effect ){
            when(effect){
                is SettingsEffect.Navigate -> {
                    navController.navigate((effect as SettingsEffect.Navigate).route)
                    settingsViewModel.resetEffect()
                }
                null -> {}
            }
        }
        SettingsScreen(state, settingsViewModel::onEvent)
    }
}

@Composable
fun SettingsScreen(state: SettingsState, onEvent: (SettingsEvent) -> Unit) {
    Scaffold(
        topBar = {
            AppBar(
                translatableRes(R.string.settings),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = ""
                    )
                },
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), contentPadding = PaddingValues(8.dp)
        ) {
            item {
                Text(text = stringResource(id = R.string.app_settings))
                Spacer(modifier = Modifier.height(6.dp))
                SettingsSwitchCard(
                    text = stringResource(id = R.string.dark_mode),
                    icon = painterResource(id = R.drawable.dark_mode_icon),
                    isChecked = state.isDarkMode,
                    onCheckedChange = {onEvent(SettingsEvent.OnDarkModeChange(it))})
                Spacer(modifier = Modifier.height(6.dp))
                SettingsComponent(
                    settingHeaderText = stringResource(id = R.string.currency),
                    label = state.currencyCode,
                    painterResourceID = painterResource(
                        id = R.drawable.currency_icon
                    )
                ) {
                    onEvent(SettingsEvent.NavigateTo(Screens.CurrencyScreen.route))
                }
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = stringResource(id = R.string.import_export))
                Spacer(modifier = Modifier.height(6.dp))
                SettingsComponent(
                    settingHeaderText = stringResource(id = R.string.back_up),
                    painterResourceID = painterResource(
                        id = R.drawable.backup_icon
                    )
                ) {
                    onEvent(SettingsEvent.NavigateTo(Screens.BackupScreens.route))

                }

            }
        }
    }
}

@Preview(
    showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Preview(
    showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
fun SettingsPreview() {
    MasrofyTheme {
        SettingsScreen(SettingsState()){}
    }
}