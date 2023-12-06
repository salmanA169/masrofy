package com.masrofy.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.masrofy.core.backup.BackUpDataFileInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportFilesDialog(
    files:List<BackUpDataFileInfo>,
    onClickFile:(String)->Unit,
    onDismiss:()->Unit
) {
    AlertDialog(onDismissRequest = onDismiss) {
        LazyColumn(){
            items(files){
                Text(text = "${it.fileName} and ${it.idFile}", maxLines = 1)
                Button(onClick = { /*TODO*/ }) {
                    
                }
            }
        }
    }
}