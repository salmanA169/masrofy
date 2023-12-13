package com.masrofy.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp

data class MenuItem(
    val title: TranslatableString,
    val icon: com.masrofy.component.Icons? = null,
    val enable: Boolean = true,
    val onClick: () -> Unit = {}
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarTextButtonMenu(
    title: TranslatableString? = null,
    navigationIcon: @Composable (() -> Unit) = {},
    menuItem: List<MenuItem> = listOf(),
) {
    val titleComposable: @Composable () -> Unit = {
        title?.let {
            H2(text = it.getString())
        }
    }
    TopAppBar(
        title = titleComposable,
        navigationIcon = navigationIcon,
        actions = {
            menuItem.forEach { menuItem ->
                OutlinedButton(shape = RoundedCornerShape(6.dp), onClick = menuItem.onClick) {
                    Text(text = menuItem.title.getString())
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: TranslatableString? = null,
    navigationIcon: @Composable (() -> Unit) = {},
    menuItem: List<MenuItem> = listOf(),
) {
    val titleComposable: @Composable () -> Unit = {
        title?.let {
            H2(text = it.getString())
        }
    }

    TopAppBar(
        title = titleComposable,
        navigationIcon = navigationIcon,
        actions = {
            menuItem.forEach { menuItem ->
                if (menuItem.icon != null) {
                    AppBarMenuButton(
                        icon = menuItem.icon.getIcon(),
                        onClick = menuItem.onClick,
                        enabled = menuItem.enable,
                        description = menuItem.title.getString(),
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable(
                                enabled = menuItem.enable,
                                onClick = menuItem.onClick
                            ),
                        text = menuItem.title.getString().toUpperCase(Locale.current),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    )
}