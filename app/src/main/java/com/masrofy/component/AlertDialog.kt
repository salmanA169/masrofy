package com.masrofy.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.masrofy.R

@Composable
fun TextDialog(
    title:String,
    desc:String,
    showDialog :Boolean ,
    onConfirm:()->Unit,
    onDismiss:()->Unit,
) {

    if (showDialog){
        AlertDialog(onDismissRequest = onDismiss, confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(id = R.string.confirm))
            }
        },title = { Text(text = title)},text = { Text(text = desc)})

    }
}