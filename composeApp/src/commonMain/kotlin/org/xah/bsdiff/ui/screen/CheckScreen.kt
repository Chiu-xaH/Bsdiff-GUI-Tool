package org.xah.bsdiff.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.xah.bsdiff.logic.util.pickFile
import org.xah.bsdiff.ui.component.RowCenter
import org.xah.bsdiff.ui.component.TransplantListItem

@Composable
fun CheckScreen() {
    var oldFilePath by remember { mutableStateOf<String?>(null) }
    var newFilePath by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf<Boolean?>(null) }

    val cor = rememberCoroutineScope()
    LaunchedEffect(newFilePath,oldFilePath) {
        isSuccess = null
    }

    if(!loading) {
        Column {
            Row {
                TransplantListItem(
                    supportingContent = oldFilePath?.let { { Text(it) } },
                    headlineContent = { Text("选择原文件") },
                    modifier = Modifier.weight(.5f).clickable {
                        cor.launch { oldFilePath = pickFile() }
                    }
                )
                TransplantListItem(
                    supportingContent = newFilePath?.let { { Text(it) } },
                    headlineContent = { Text("选择合成的新文件") },
                    modifier = Modifier.weight(.5f).clickable {
                        cor.launch { newFilePath = pickFile() }
                    }
                )
            }
            Spacer(Modifier.height(10.dp))
            RowCenter {
                Button(
                    onClick = {
                        oldFilePath?.let {
                            newFilePath?.let { it1 ->
                                isSuccess = isSimpleFile(it, it1) { load ->
                                    loading = load
                                }
                            }
                        }
                    },
                    enabled = canStartPatch(oldFilePath,newFilePath),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Text("校验完整性")
                }
            }
        }
    }
    DoLoadingUI(null,loading,isSuccess,true) {
        isSuccess = null
    }
}

expect fun isSimpleFile(path1 : String,path2 : String, callback: (Boolean) -> Unit) : Boolean

