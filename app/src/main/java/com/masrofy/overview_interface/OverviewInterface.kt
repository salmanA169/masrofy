package com.masrofy.overview_interface

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface OverviewInterface<T> {
    fun getIcon():Int
    fun getLabel():Int
    @Composable
    fun GetContent(modifier: Modifier)
    val data : T
}