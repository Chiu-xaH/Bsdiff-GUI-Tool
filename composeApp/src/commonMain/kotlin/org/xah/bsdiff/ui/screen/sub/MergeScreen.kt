package org.xah.bsdiff.ui.screen.sub

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.xah.bsdiff.logic.util.mergePatch
import org.xah.bsdiff.logic.util.openFileExplorer
import org.xah.bsdiff.logic.util.pickFile
import org.xah.bsdiff.ui.component.StyleCardListItem
import org.xah.bsdiff.ui.component.TransplantListItem
import org.xah.bsdiff.ui.screen.DoLoadingUI
import org.xah.bsdiff.ui.util.DropUI

@Composable
fun MergeScreen() {
    var oldFilePath by remember { mutableStateOf<String?>(null) }
    var newFilePath by remember { mutableStateOf<String>("") }
    var patchFilePath by remember { mutableStateOf<String?>(null) }
    var oldFileName by remember { mutableStateOf("") }
    var patchFileName by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf<Boolean?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(patchFilePath,oldFilePath) {
        isSuccess = null
        patchFilePath?.let { newFilePath = getDefaultPath(it) }
        oldFilePath?.let { oldFileName = getFileName(it) }
        patchFilePath?.let { patchFileName = getFileName(it) }
    }

    if(!loading) {
        Box {
            Row(modifier = Modifier.padding(5.dp)) {
                DropUI("旧文件", modifier = Modifier.fillMaxSize().weight(.5f)) {
                    oldFilePath = it
                }
                Spacer(Modifier.width(5.dp))
                DropUI("补丁包(.patch)", modifier = Modifier.fillMaxSize().weight(.5f)) {
                    patchFilePath = it
                }
            }
            Column {
                Row {
                    TransplantListItem(
                        headlineContent = { Text("选择或拖入旧文件") },
                        supportingContent = oldFilePath?.let { { Text(it) } },
                        modifier = Modifier.weight(.5f).clickable {
                            scope.launch { oldFilePath = pickFile() }
                        }
                    )
                    TransplantListItem(
                        headlineContent = { Text("选择或拖入补丁文件(.patch)") },
                        supportingContent = patchFilePath?.let { { Text(it) } },
                        modifier = Modifier.weight(.5f).clickable {
                            scope.launch { patchFilePath = pickFile() }
                        }
                    )
                }
                if(newFilePath.isNotEmpty() && patchFilePath != null && oldFilePath != null) {
                    val newFileName = oldFileName + "_merged." + getFileExtension(oldFileName)
                    StyleCardListItem(
                        headlineContent = { newFilePath.let{ Text(it, textDecoration = TextDecoration.Underline, modifier = Modifier.clickable { openFileExplorer(it) }) }},
                        supportingContent = { Text("生成${newFileName}到目录") },
                        trailingContent = {
                            Button(
                                onClick = {
                                    scope.launch {
                                        loading = true
                                        // 开始生成补丁包
                                        isSuccess = mergePatch(oldFilePath!!, patchFilePath!!, applyPath(newFilePath ,newFileName))
                                        loading = false
                                    }
                                },
                                enabled = canStartMerge(patchFilePath,oldFilePath),
                                shape = MaterialTheme.shapes.medium,
                            ) {
                                Text("生成合并包")
                            }
                        }
                    )
                }
            }
        }
    }
    DoLoadingUI(newFilePath, loading, isSuccess) {
        isSuccess = null
    }
}


private fun canStartMerge(path1 : String?, path2: String?) : Boolean {
    // 未选择文件
    if(path1 == null || path2 == null) {
        return false
    }
    // 选择了同一个文件
    if(path1 == path2) {
        return false
    }
    return true
}