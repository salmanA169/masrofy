package com.masrofy.screens.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masrofy.Screens

fun NavGraphBuilder.categoriesDest(navController: NavController){
    composable(Screens.CategoriesScreen.formatRoute,Screens.CategoriesScreen.args){
        CategoriesScreen()
    }
}

@Composable
fun CategoriesScreen() {
  Column() {
      // TODO: continue here implement viewModel and state and ui
      Text("salman")
  }
}