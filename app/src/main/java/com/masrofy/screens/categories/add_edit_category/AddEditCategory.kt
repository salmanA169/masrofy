package com.masrofy.screens.categories.add_edit_category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masrofy.R
import com.masrofy.Screens
import com.masrofy.component.AppBar
import com.masrofy.component.TranslatableString
import com.masrofy.screens.categories.CategoryEvent

fun NavGraphBuilder.addEditCategoryDest(navController: NavController) {
    composable(Screens.AddEditCategoryScreen.formatRoute, Screens.AddEditCategoryScreen.args) {
        val viewModel = hiltViewModel<AddEditCategoryViewModel>()
        val state by viewModel.categoryState.collectAsStateWithLifecycle()
        val effect by viewModel.categoryEffect.collectAsStateWithLifecycle()
        LaunchedEffect(key1 = effect) {
            when (effect) {
                AddEditCategoryEffect.Nothing -> Unit
                AddEditCategoryEffect.Saved -> {
                    navController.popBackStack()
                }

                AddEditCategoryEffect.ClosePage -> {
                    navController.popBackStack()
                }
            }
        }
        AddEditCategoryScreen(state, viewModel::onEvent)
    }
}

@Composable
fun AddEditCategoryScreen(
    state: AddEditCategoryState,
    onEvent: (AddEditCategoryEvent) -> Unit = {}
) {
    Scaffold (
        topBar = {
            AppBar(
                TranslatableString.ResString(R.string.categories,""),
                {
                    IconButton(onClick = {
                        onEvent(AddEditCategoryEvent.ClosePage)
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                listOf()
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(8.dp)
                .statusBarsPadding()
        ) {
            TextField(
                value = state.category,
                onValueChange = { onEvent(AddEditCategoryEvent.OnChangeText(it)) },
                isError = state.showError,
                supportingText = {
                    if (state.showError) {
                        Text(text = state.errorMessage)
                    }
                },
                modifier = Modifier.fillMaxWidth().testTag("edit category"),

                )
            Button(
                onClick = { onEvent(AddEditCategoryEvent.Save) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.save),modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}