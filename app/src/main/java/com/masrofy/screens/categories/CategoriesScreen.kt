package com.masrofy.screens.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.masrofy.component.MenuItem
import com.masrofy.component.TranslatableString

fun NavGraphBuilder.categoriesDest(navController: NavController){
    composable(Screens.CategoriesScreen.formatRoute,Screens.CategoriesScreen.args){
        val  categoryViewModel = hiltViewModel<CategoriesViewModel>()
        val state by categoryViewModel.categoryState.collectAsStateWithLifecycle()
        val effect by categoryViewModel.categoryEffect.collectAsStateWithLifecycle()
        LaunchedEffect(key1 = effect ){
            when(effect){
                CategoryEffect.Nothing -> {}
                is CategoryEffect.OnNavigate -> {
                    navController.navigate((effect as CategoryEffect.OnNavigate).route)
                }
                CategoryEffect.OnPopBack -> {
                    navController.popBackStack()
                }
            }
            categoryViewModel.resetEffect()
        }
        CategoriesScreen(state,categoryViewModel::onEvent)
    }
}

@Composable
fun CategoriesScreen(
    categoriesState: CategoriesState,
    onEvent:(CategoryEvent)->Unit = {}
) {
  Column() {
      Scaffold(
          topBar = {
              AppBar(
                  title =TranslatableString.ResString(R.string.categories,categoriesState.title),
                  navigationIcon = {
                      IconButton(onClick = {  onEvent(CategoryEvent.PopBack) }) {
                          Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                      }
                  },
                  menuItem = listOf(
                      MenuItem(
                          TranslatableString.PlainString(""),
                          com.masrofy.component.Icons.VectorIcon(Icons.Default.Add),
                          onClick = {
                              onEvent(CategoryEvent.NavigateToAddEditCategory(-1))
                          }
                      )
                  )
              )
          }
      ) {
          LazyColumn(modifier = Modifier
              .fillMaxSize()
              .padding(it)){
              items(categoriesState.categories, key = {it.id}, contentType = {it.nameCategory}){
                  CategoryItem(title = it.nameCategory,isDeletable = it.isPrimary, it.id,onEvent)
                  Divider()
              }
          }
      }
  }
}

@Composable
fun CategoryItem(
    title:String,
    isDeletable:Boolean,
    categoryId:Int,
    onEvent: (CategoryEvent) -> Unit
) {

    var deleteId by rememberSaveable {
        mutableStateOf<Int?>(null)
    }
    if (deleteId!=null){
        // TODO: improve
        AlertDialog(onDismissRequest = { deleteId = null }, confirmButton = {
            TextButton(onClick = {
                onEvent(CategoryEvent.OnDelete(deleteId!!))
                deleteId = null
            }) {
                Text(text = stringResource(id = R.string.confirm))
            }
        })
    }
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .padding(8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        Text(text = title, style = MaterialTheme.typography.labelMedium,color = MaterialTheme.colorScheme.secondary)
        Spacer(modifier = Modifier.weight(1f))


        if (!isDeletable){
            IconButton(onClick = {
                onEvent(CategoryEvent.NavigateToAddEditCategory(categoryId))
            }) {
                Icon(imageVector = Icons.Outlined.Edit, contentDescription =null)
            }
            IconButton(onClick = { deleteId = categoryId}) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription =null, tint = MaterialTheme.colorScheme.error)
            }
        }

        // TODO: add reorder icon and implement it
    }
}