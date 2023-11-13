package com.masrofy.screens.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.repository.TransactionRepository
import com.masrofy.utils.editDarkMode
import com.masrofy.utils.getDarkModeFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val dispatcherProvider: DispatcherProvider,
    private val repository: TransactionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()


    private val _effect = MutableStateFlow<SettingsEffect?>(null)
    val effect = _effect.asStateFlow()
    init {
        viewModelScope.launch(dispatcherProvider.io) {
            dataStore.getDarkModeFlow().collect { isDarkMode ->
                _state.update {
                    it.copy(isDarkMode)
                }
            }
        }
        viewModelScope.launch(dispatcherProvider.io) {
            repository.getAccountFlow().collect{accounts->
                _state.update {
                    it.copy(
                        currencyCode = accounts.first().currency.currencyCode
                    )
                }
            }
        }
    }

    fun onEvent(settingsEvent: SettingsEvent) {
        when (settingsEvent) {
            is SettingsEvent.OnDarkModeChange -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    dataStore.editDarkMode(settingsEvent.darkMode)
                }
            }

            is SettingsEvent.NavigateTo -> {

                _effect.update {
                    SettingsEffect.Navigate(settingsEvent.route)
                }
            }
        }
    }
    fun resetEffect(){
        _effect.update {
            null
        }
    }
}

sealed class SettingsEffect {
    class Navigate(val route: String) : SettingsEffect()
}

sealed class SettingsEvent {
    class OnDarkModeChange(val darkMode: Boolean) : SettingsEvent()
    class NavigateTo(val route: String) : SettingsEvent()
}