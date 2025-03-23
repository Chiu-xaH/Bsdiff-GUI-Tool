package org.xah.bsdiff.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.xah.bsdiff.logic.util.createPatch
import org.xah.bsdiff.logic.util.openFileExplorer
import org.xah.bsdiff.logic.util.pickFile
import org.xah.bsdiff.ui.component.MyDialog
import org.xah.bsdiff.ui.component.StyleCardListItem
import org.xah.bsdiff.ui.component.TransplantListItem

@Composable
fun SplitScreen() {
    var oldFilePath by remember { mutableStateOf<String?>(null) }
    var newFilePath by remember { mutableStateOf<String?>(null) }
    var patchFilePath by remember { mutableStateOf("") }
    var oldFileName by remember { mutableStateOf("") }
    var newFileName by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf<Boolean?>(null) }
    val cor = rememberCoroutineScope()

    LaunchedEffect(newFilePath,oldFilePath) {
        isSuccess = null
        newFilePath?.let { patchFilePath = getDefaultPath(it) }
        oldFilePath?.let { oldFileName = getFileName(it) }
        newFilePath?.let { newFileName = getFileName(it) }
    }

    if(!loading) {
        Column {
            Row {
                TransplantListItem(
                    headlineContent = { Text("选择旧文件") },
                    supportingContent = oldFilePath?.let { { Text(it) } },
                    modifier = Modifier.weight(.5f).clickable {
                        cor.launch { oldFilePath = pickFile() }
                    }
                )
                TransplantListItem(
                    headlineContent = { Text("选择新文件") },
                    supportingContent = newFilePath?.let { { Text(it) } },
                    modifier = Modifier.weight(.5f).clickable {
                        cor.launch { newFilePath = pickFile() }
                    }
                )
            }
            if(patchFilePath.isNotEmpty() && oldFilePath != null && newFilePath != null) {
                val patchFileName = getPatchFileName(newFileName, oldFileName)
                StyleCardListItem(
                    headlineContent = { Text(patchFilePath) },
                    supportingContent = { Text("生成${patchFileName}到目录") },
                    trailingContent = {
                        Button(
                            onClick = {
                                cor.launch {
                                    // 开始生成补丁包
                                    isSuccess = createPatch(oldFilePath!!, newFilePath!!, applyPath(patchFilePath, patchFileName)) { load ->
                                        loading = load
                                    }
                                }
                            },
                            enabled = canStartPatch(newFilePath,oldFilePath),
                            shape = MaterialTheme.shapes.medium,
                        ) {
                            Text("生成补丁包")
                        }
                    }
                )
            }
        }
    }
    DoLoadingUI(patchFilePath,loading,isSuccess)
}

@Composable
fun DoLoadingUI(path : String?, loading : Boolean, isSuccess : Boolean?, isCheck : Boolean = false) {
    if(loading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(isSuccess) {
        if(isSuccess != null) {
            showDialog = true
        }
    }

    // 结束后弹窗 并调用onLoading回调关闭加载
    if(showDialog) {
        MyDialog(
            onConfirmation = { showDialog = false },
            onDismissRequest = {
                if(isSuccess == true && !isCheck) {
                    path?.let { openFileExplorer(it) }
                } else {
                    showDialog = false
                }
            },
            dialogText = if(isCheck) {
                if(isSuccess == true)"校验为同一文件" else "非同一文件"
            } else {
                if(isSuccess == true)"生成成功" else "生成失败"
            },
            dismissText = if(isSuccess == true && !isCheck) "打开文件夹" else "好",
        )
    }
}


fun canStartPatch(path1 : String?, path2: String?) : Boolean {
    // 未选择文件
    if(path1 == null || path2 == null) {
        return false
    }
    // 选择了同一个文件
    if(path1 == path2) {
        return false
    }
    // 扩展名必须一致
    if(getFileExtension(path1) != getFileExtension(path2)) {
        return false
    }
    return true
}

fun getPatchFileName(newFileName : String,oldFileName : String) : String {
    // 补丁名为 旧文件_patched_新文件.新文件的扩展名
    val e = getFileExtension(newFileName)
    val o = getFileNameWithoutExtension(oldFileName)
    val n = getFileNameWithoutExtension(newFileName)
    return o + "_to_" + n + "." + "patch"
}

// 在共享模块中定义expect函数
expect fun getDefaultPath(path: String): String
// 在共享模块中定义expect函数
expect fun getFileName(path: String): String

fun getFileExtension(fileName: String): String = fileName.substringAfterLast(".", "")

fun getFileNameWithoutExtension(fileName: String): String = fileName.substringBeforeLast(".") // 去掉扩展名
// 连接
expect fun applyPath(path: String,file : String) : String