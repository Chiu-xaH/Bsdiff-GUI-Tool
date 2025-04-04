package org.xah.bsdiff.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.xah.bsdiff.logic.util.openFileExplorer
import org.xah.bsdiff.logic.util.sendNotice
import org.xah.bsdiff.ui.component.MyDialog

@Composable
fun DoLoadingUI(path : String?, loading : Boolean, isSuccess : Boolean?, isCheck : Boolean = false,onSuccess : (Boolean?) -> Unit) {
    if(loading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    var showDialog by remember { mutableStateOf(false) }

    if(isSuccess != null) {
        sendNotice()
        showDialog = true
    }

    // 结束后弹窗 并调用onLoading回调关闭加载
    if(showDialog) {
        MyDialog(
            onConfirmation = {
                onSuccess.invoke(null)
                showDialog = false
            },
            onDismissRequest = {
                if(isSuccess == true && !isCheck) {
                    path?.let { openFileExplorer(it) }
                } else {
                    onSuccess.invoke(null)
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
