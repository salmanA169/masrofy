package com.masrofy.component

import android.widget.Toast
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.masrofy.R
import com.masrofy.core.backup.BackUpDataFileInfo
import com.masrofy.core.backup.ProgressState
import com.masrofy.ui.theme.MasrofyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportFilesDialog(
    files:List<BackUpDataFileInfo>,
    progressDownloadState: ProgressDownloadState,
    onClickFile:(String)->Unit,
    onDismiss:()->Unit
) {
    val context = LocalContext.current
    AlertDialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Card(elevation = CardDefaults.cardElevation(6.dp), shape = RoundedCornerShape(4.dp)) {
            LazyColumn(){
                items(files){
                    ElevatedCard(onClick = {onClickFile(it.idFile)}, shape = RoundedCornerShape(0.dp)) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Icon(painter = painterResource(id = R.drawable.backup_icon), contentDescription = "")
                            Text(text = "${it.fileName} ", fontSize = 17.sp, maxLines = 1)
                            if (progressDownloadState.fileId == it.idFile){
                                if (progressDownloadState.state == ProgressState.INITIATION_STARTED){
                                    CircularProgressIndicator()
                                }else if (progressDownloadState.state == ProgressState.STARTED){
                                    ProgressWithText(progress = progressDownloadState.progress * 100,modifier = Modifier.wrapContentSize())
                                }else if (progressDownloadState.state == ProgressState.COMPLETE){
                                    Toast.makeText(context, "successfully Imported", Toast.LENGTH_SHORT).show()
                                    onDismiss()
                                }
                            }else{
                                Text(text = it.size)
                            }
                        }
                    }
                }
            }
        }
    }
}
data class ProgressDownloadState(
    val state :ProgressState = ProgressState.NOT_STARTED,
    val fileId:String = "",
    val progress : Float = 0f
)
