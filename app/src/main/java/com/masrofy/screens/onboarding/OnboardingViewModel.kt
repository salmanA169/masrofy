package com.masrofy.screens.onboarding

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masrofy.ONBOARDING_IS_FIRST_TIME
import com.masrofy.ONBOARDING_SCREENS_ARGS
import com.masrofy.coroutine.DispatcherProvider
import com.masrofy.currency.COUNTRY_DATA
import com.masrofy.currency.CURRENCY_DATA
import com.masrofy.currency.Currency
import com.masrofy.data.database.MasrofyDatabase
import com.masrofy.emoji.EmojiData
import com.masrofy.onboarding.OnBoardingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val db: MasrofyDatabase,
    savedStateHandle: SavedStateHandle,
    private val dataStore: DataStore<Preferences>,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _onboardingState = MutableStateFlow(OnboardingState())
    val onboardingState = _onboardingState.asStateFlow()

    private val isFirstTime = savedStateHandle.get<Boolean>(ONBOARDING_IS_FIRST_TIME)!!
    private val screensOnboarding = savedStateHandle.get<Array<String>>(ONBOARDING_SCREENS_ARGS)!!

    private val onboardingManager = OnBoardingManager(db,dataStore)

    private val _effect = MutableStateFlow<OnboardingEffect?>(null)
     val effect = _effect.asStateFlow()

    init {
        _onboardingState.update {
            it.copy(
                currencyItems = initialCurrencyItems(),
                currentCountryCode = Locale.getDefault().country
            )
        }
    }
    fun onEvent(event: OnboardingEvent) {
        when (event) {
            OnboardingEvent.Back -> back()
            is OnboardingEvent.CurrencyData -> {
                onboardingManager.setCurrentCurrencyOnboardingData(
                    event.currency
                )
                _onboardingState.update {
                    it.copy(
                        selectedCurrency = event.currency
                    )
                }
                updateValue()
            }

            OnboardingEvent.Next -> next()
            OnboardingEvent.Finish -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    onboardingManager.finish()

                    _effect.update {
                        OnboardingEffect.Close
                    }
                }
            }
        }
    }

    fun resetEffect(){
        _effect.update {
            null
        }
    }
    private fun next() {
        onboardingManager.next()
        updateValue()
    }

    private fun back() {
        onboardingManager.back()
        updateValue()
    }

    init {
        onboardingManager.setData(isFirstTime, screensOnboarding.toList())
        updateValue()
    }


    private fun updateValue() {
        _onboardingState.update {
            it.copy(
                onboardingManager.screens,
                onboardingManager.isFirstScreen(),
                onboardingManager.canMove(),
                onboardingManager.isLastScreen(),
                onboardingManager.currentIndex,
                if (onboardingManager.isLastScreen()) "Finish" else "Next"
            )
        }
    }


}

sealed class OnboardingEvent {
    object Next : OnboardingEvent()
    object Back : OnboardingEvent()
    class CurrencyData(val currency: Currency) : OnboardingEvent()
    object Finish:OnboardingEvent()
}

sealed class OnboardingEffect{
    object Close :OnboardingEffect()
}