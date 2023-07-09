package com.masrofy.overview_interface

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.masrofy.R

interface OverviewInterface<T> {
    fun getIcon():Int
    fun getLabel():Int
    @Composable
    fun GetContent(modifier: Modifier)
    val data : T
}

interface BaseOverView<T>:OverviewInterface<T>{


    @Composable
    fun BaseOverViewScreen(
        modifier: Modifier,
        content:@Composable ColumnScope.()->Unit
    ) {
        Column(modifier = modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = getIcon()),
                    modifier = Modifier.size(20.dp),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(id = getLabel()),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Divider()
            content()
        }
    }
}