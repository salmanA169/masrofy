package com.masrofy.overview_interface

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.masrofy.R

interface OverviewInterface<T> {
    fun getIcon():Int
    fun getLabel():Int
    @Composable
    fun GetContent(modifier: Modifier)
    val data : T
}

interface BaseOverView<T>:OverviewInterface<T>{
    override fun getIcon(): Int {
        return R.drawable.statistic_icon1
    }

    override fun getLabel(): Int {
        return R.string.this_week
    }

    @Composable
    fun () {
        
    }
}