package com.masrofy.screens.onboarding

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masrofy.R
import com.masrofy.Screens
import com.masrofy.currency.Currency
import com.masrofy.onboarding.CURRENCY_SCREEN_TYPE
import com.masrofy.onboarding.WELCOME_SCREEN_TYPE
import com.masrofy.ui.theme.MasrofyTheme
import com.masrofy.ui.theme.SurfaceColor
import com.masrofy.utils.cellShape
import com.masrofy.utils.shouldShowDivider
import kotlin.random.Random

fun NavGraphBuilder.onBoardingDest(navController: NavController) {
    composable(
        Screens.OnboardingScreen.formatRoute,
        Screens.OnboardingScreen.args
    ) {
        val onboardingViewModel = hiltViewModel<OnboardingViewModel>()
        val onboardingScreen by onboardingViewModel.onboardingState.collectAsStateWithLifecycle()
        val onboardingScreenEffect by onboardingViewModel.effect.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = onboardingScreenEffect) {
            when (onboardingScreenEffect) {
                OnboardingEffect.Close -> navController.navigate(Screens.MainScreen.route) {
                    popUpTo(Screens.OnboardingScreen.formatRoute) {
                        inclusive = true
                    }
                }

                null -> Unit
            }
        }
        OnBoardingScreen(onboardingState = onboardingScreen, onboardingViewModel::onEvent)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
    onboardingState: OnboardingState,
    onEvent: (OnboardingEvent) -> Unit = {},
) {
    val rememberPageState = rememberPagerState(0) { onboardingState.screens.size }
    LaunchedEffect(key1 = onboardingState.currentIndex) {
        rememberPageState.animateScrollToPage(onboardingState.currentIndex)
    }

    val data by onboardingState.rememberGroupedCurrencyItems()
    val localDensity = LocalDensity.current
    var padding by remember {
        mutableStateOf(0.dp)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()

    ) {
        HorizontalPager(
            userScrollEnabled = false,
            state = rememberPageState,
            modifier = Modifier.padding(bottom = padding),
            key = { it },
        ) {
            when (onboardingState.screens[it].screenType) {
                WELCOME_SCREEN_TYPE -> {
                    WelcomeScreen()
                }

                CURRENCY_SCREEN_TYPE -> {
                    CurrencyList(data, onboardingState.selectedCurrency) {
                        onEvent(OnboardingEvent.CurrencyData(it.currency))
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .onSizeChanged {
                    with(localDensity) {
                        padding = it.height.toDp()
                    }

                }
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            if (!onboardingState.isFirstScreen) {
                Button(onClick = {
                    onEvent(OnboardingEvent.Back)
                }) {
                    Text(text = "Back")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                enabled = onboardingState.canMove,
                onClick = {
                    if (onboardingState.isLastScreen) onEvent(OnboardingEvent.Finish) else onEvent(
                        OnboardingEvent.Next
                    )
                }) {
                Text(text = onboardingState.label)
            }
        }

    }


}

@Composable
fun CurrencyList(
    data: Map<String, List<CurrencyItem>>,
    currentCurrencySelect: Currency?,
    onCurrencyClick: (CurrencyItem) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        CurrencyCell(
            data, currentCurrencySelect, onCurrencyClick
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
 fun LazyListScope.CurrencyCell(
    data: Map<String, List<CurrencyItem>>,
    selectedCurrency: Currency?,
    onItemClick: (CurrencyItem) -> Unit,
) {

    data.forEach { (title, currencies) ->
        if (title.isNotBlank()) {
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .background(color = MaterialTheme.colorScheme.surface)
                ) {
                    Text(
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp
                        ),
                        text = title,
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
                    )
                }
            }
        }

        val size = currencies.size
        itemsIndexed(
            items = currencies,
            // TODO: fix it very important
            key = { index, item -> item.hashCode() + Random.nextInt()  }
        ) { index, item ->
            CurrencyItemCell(
                currencySymbol = item.currencySymbol,
                flag = item.flag,
                countryName = item.countryName,
                selected = item.currency == selectedCurrency,
                shape = cellShape(index, size),
                showDivider = shouldShowDivider(index, size),
                onClick = { onItemClick(item) }
            )
        }
    }
}

@Composable
private fun CurrencyItemCell(
    currencySymbol: String,
    flag: String,
    countryName: String,
    selected: Boolean,
    shape: Shape,
    showDivider: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(shape)
            .clickable(onClick = onClick),
        shape = shape,
        color = if (!selected) SurfaceColor.surfaces.surfaceContainerHighest else MaterialTheme.colorScheme.primaryContainer,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(modifier = Modifier.weight(.75F)) {
                    Text(
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp
                        ),
                        text = flag,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    Text(
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp
                        ),
                        text = countryName,
                    )
                }

                Row(
                    modifier = Modifier.weight(.25f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        style = MaterialTheme.typography.headlineMedium, fontSize = 16.sp,
                        text = currencySymbol,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .align(alignment = Alignment.CenterVertically)
                    )


                }
            }

            if (showDivider) {

                Divider(
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}


@Composable
fun WelcomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val animateVisible = remember {
            Animatable(0f)
        }
        LaunchedEffect(key1 = true) {
            animateVisible.animateTo(1f, animationSpec = tween(1500))
        }
        Image(
            painter = painterResource(id = R.drawable.onboarding_money),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .alpha(animateVisible.value)
        )
        Text(
            text = "Welcome To Masrofy App",
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.inknutantiqua_bold)),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Take Control of your money",
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.inknutantiqua_light))
        )
        Text(
            text = "and save them by tracking your expense ",
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.inknutantiqua_light))
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    MasrofyTheme {
        WelcomeScreen()
    }
}